package com.example.checkncare.tflite

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import kotlin.math.*

/**
 * Preprocessing pipeline verified to match librosa / Colab exactly:
 *
 *  1. Amplitude-normalize: y / (max|y| + 1e-8)
 *  2. Center-pad: reflect-pad by n_fft/2 on each side
 *  3. Periodic Hann window: w[i] = 0.5 * (1 - cos(2π·i / N_FFT))   ← divide by N_FFT
 *  4. STFT power: |rfft(windowed)|²  per frame
 *  5. Mel filterbank with Slaney normalization (librosa default)
 *  6. power_to_db: 10·log10(max(S,1e-10)), then clamp to (max - 80 dB)
 *  7. Per-clip min-max normalize to [0, 1], scale to [0, 255] uint8
 *  8. Resize to 224×224, treat rows as-is (no flip), send raw [0,255] floats
 */
class AudioClassifier(private val context: Context) {

    companion object {
        private const val TAG = "AudioClassifier"
    }

    private var interpreter: Interpreter? = null
    private val modelPath = "AudioModel.tflite"
    private val imageSize = 224

    private val sampleRate = 16000
    private val nFft       = 2048
    private val hopLength  = 512
    private val nMels      = 128
    private val fMin       = 0.0
    private val fMax       = sampleRate / 2.0
    private val topDb      = 80.0   // librosa power_to_db default

    // Pre-compute periodic Hann window and mel filterbank once
    private val hannWindow: FloatArray = FloatArray(nFft) {
        (0.5 * (1.0 - cos(2.0 * PI * it / nFft))).toFloat()   // periodic: divide by N, not N-1
    }
    private val melFilterbank: Array<FloatArray> = buildMelFilterbank()

    init {
        try {
            interpreter = Interpreter(loadModelFile())
            Log.d(TAG, "Model loaded OK — input ${interpreter!!.getInputTensor(0).shape().toList()}")
        } catch (e: Exception) {
            Log.e(TAG, "Model load failed: ${e.message}", e)
        }
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fd = context.assets.openFd(modelPath)
        return FileInputStream(fd.fileDescriptor).channel.map(
            FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.length
        )
    }

    fun classify(audioData: ShortArray): AudioResult {
        if (interpreter == null) {
            Log.e(TAG, "Interpreter is null")
            return AudioResult("Error", 0f)
        }

        // 1. PCM → float
        val floatAudio = FloatArray(audioData.size) { audioData[it] / 32768f }

        // 2. Amplitude normalisation
        val maxAbs = floatAudio.maxOfOrNull { abs(it) } ?: 1f
        val y = FloatArray(floatAudio.size) { floatAudio[it] / (maxAbs + 1e-8f) }

        // 3. Compute log-mel spectrogram
        val spec = computeLogMelSpectrogram(y)

        // 4. Render bitmap (no row flip — PIL.fromarray keeps row 0 at top)
        val rows   = spec.size
        val cols   = spec[0].size
        val bitmap = Bitmap.createBitmap(cols, rows, Bitmap.Config.ARGB_8888)
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val v = (spec[row][col] * 255f).toInt().coerceIn(0, 255)
                bitmap.setPixel(col, row, Color.rgb(v, v, v))
            }
        }
        val resized = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true)

        // 5. Pack raw [0,255] float values — model has internal Rescaling layer
        val buf = ByteBuffer.allocateDirect(imageSize * imageSize * 3 * 4)
        buf.order(ByteOrder.nativeOrder())
        val pixels = IntArray(imageSize * imageSize)
        resized.getPixels(pixels, 0, imageSize, 0, 0, imageSize, imageSize)
        for (px in pixels) {
            buf.putFloat((px shr 16 and 0xFF).toFloat())
            buf.putFloat((px shr 8  and 0xFF).toFloat())
            buf.putFloat((px        and 0xFF).toFloat())
        }

        // 6. Inference
        val output = Array(1) { FloatArray(2) }
        interpreter!!.run(buf, output)
        val probs = output[0]
        Log.d(TAG, "Output: Healthy=${probs[0]}, Sick=${probs[1]}")

        val idx   = if (probs[0] >= probs[1]) 0 else 1
        val label = if (idx == 0) "Normal" else "Sick"
        return AudioResult(label, probs[idx])
    }

    // -----------------------------------------------------------------------

    private fun computeLogMelSpectrogram(audio: FloatArray): Array<FloatArray> {
        val halfFft   = nFft / 2 + 1
        // Center-pad by n_fft/2 on each side (reflect padding, matches librosa)
        val pad       = nFft / 2
        val padded    = FloatArray(audio.size + 2 * pad)
        // reflect left edge
        for (i in 0 until pad) padded[pad - 1 - i] = audio[i]
        // copy audio
        audio.copyInto(padded, pad)
        // reflect right edge
        for (i in 0 until pad) padded[pad + audio.size + i] = audio[audio.size - 1 - i]

        val numFrames = 1 + audio.size / hopLength

        // Power mel spectrogram
        val powerMel = Array(nMels) { FloatArray(numFrames) }
        val real     = FloatArray(nFft)
        val imag     = FloatArray(nFft)

        for (frame in 0 until numFrames) {
            val start = frame * hopLength
            for (i in 0 until nFft) {
                val idx = start + i
                real[i] = if (idx < padded.size) padded[idx] * hannWindow[i] else 0f
                imag[i] = 0f
            }
            fftInPlace(real, imag, nFft)

            // Power spectrum |X|²
            val power = FloatArray(halfFft) { i -> real[i] * real[i] + imag[i] * imag[i] }

            // Apply Slaney-normalized mel filterbank
            for (m in 0 until nMels) {
                var sum = 0f
                val fb = melFilterbank[m]
                for (k in 0 until halfFft) sum += fb[k] * power[k]
                powerMel[m][frame] = sum
            }
        }

        // power_to_db: 10*log10(max(S, 1e-10)), then clamp to (max - 80)
        val dbMel = Array(nMels) { m ->
            FloatArray(numFrames) { f -> (10.0 * log10(maxOf(powerMel[m][f].toDouble(), 1e-10))).toFloat() }
        }
        var dbMax = -Float.MAX_VALUE
        for (m in 0 until nMels) for (f in 0 until numFrames) if (dbMel[m][f] > dbMax) dbMax = dbMel[m][f]
        val dbFloor = dbMax - topDb.toFloat()
        for (m in 0 until nMels) for (f in 0 until numFrames) if (dbMel[m][f] < dbFloor) dbMel[m][f] = dbFloor

        // Per-clip min-max normalise to [0, 1]
        var globalMin = Float.MAX_VALUE
        var globalMax = -Float.MAX_VALUE
        for (m in 0 until nMels) for (f in 0 until numFrames) {
            if (dbMel[m][f] < globalMin) globalMin = dbMel[m][f]
            if (dbMel[m][f] > globalMax) globalMax = dbMel[m][f]
        }
        val range = globalMax - globalMin
        Log.d(TAG, "dB range: $globalMin to $globalMax  frames=$numFrames")
        if (range == 0f) return Array(nMels) { FloatArray(numFrames) { 0f } }
        return Array(nMels) { m -> FloatArray(numFrames) { f -> (dbMel[m][f] - globalMin) / range } }
    }

    /**
     * Mel filterbank with Slaney normalization — matches librosa.filters.mel() default.
     *
     * Steps:
     *  1. nMels+2 centre frequencies equally spaced in mel scale
     *  2. Map each centre to the nearest FFT bin (searchsorted equivalent)
     *  3. Build triangular filters in frequency domain
     *  4. Normalize each filter by 2/(right_hz - left_hz)  (Slaney/HTK area norm)
     */
    private fun buildMelFilterbank(): Array<FloatArray> {
        val halfFft   = nFft / 2 + 1
        // FFT bin centre frequencies
        val fftFreqs  = DoubleArray(halfFft) { it * sampleRate.toDouble() / nFft }

        fun hzToMel(hz: Double) = 2595.0 * log10(1.0 + hz / 700.0)
        fun melToHz(mel: Double) = 700.0 * (10.0.pow(mel / 2595.0) - 1.0)

        val melMin = hzToMel(fMin)
        val melMax = hzToMel(fMax)

        // nMels+2 equally-spaced points in mel scale
        val melPts = DoubleArray(nMels + 2) { i -> melMin + i * (melMax - melMin) / (nMels + 1) }
        val hzPts  = DoubleArray(nMels + 2) { melToHz(melPts[it]) }

        // Map hz to FFT bin index using searchsorted (first bin >= hz_centre)
        fun searchSorted(hz: Double): Int {
            var lo = 0; var hi = halfFft
            while (lo < hi) {
                val mid = (lo + hi) ushr 1
                if (fftFreqs[mid] < hz) lo = mid + 1 else hi = mid
            }
            return lo.coerceIn(0, halfFft - 1)
        }
        val bins = IntArray(nMels + 2) { searchSorted(hzPts[it]) }

        val fb = Array(nMels) { FloatArray(halfFft) }
        for (m in 0 until nMels) {
            val lHz = hzPts[m]; val cHz = hzPts[m + 1]; val rHz = hzPts[m + 2]
            val lBin = bins[m]; val rBin = bins[m + 2]

            for (k in lBin..rBin.coerceAtMost(halfFft - 1)) {
                val f = fftFreqs[k]
                fb[m][k] = when {
                    f in lHz..cHz && cHz > lHz -> ((f - lHz) / (cHz - lHz)).toFloat()
                    f in cHz..rHz && rHz > cHz -> ((rHz - f) / (rHz - cHz)).toFloat()
                    else -> 0f
                }
            }

            // Slaney normalization: scale by 2 / (right_hz - left_hz)
            val bw = rHz - lHz
            if (bw > 0.0) {
                val scale = (2.0 / bw).toFloat()
                for (k in 0 until halfFft) fb[m][k] *= scale
            }
        }
        return fb
    }

    // Cooley-Tukey radix-2 in-place FFT (n must be a power of 2)
    private fun fftInPlace(real: FloatArray, imag: FloatArray, n: Int) {
        var j = 0
        for (i in 1 until n) {
            var bit = n shr 1
            while (j and bit != 0) { j = j xor bit; bit = bit shr 1 }
            j = j xor bit
            if (i < j) {
                var t = real[i]; real[i] = real[j]; real[j] = t
                t = imag[i]; imag[i] = imag[j]; imag[j] = t
            }
        }
        var len = 2
        while (len <= n) {
            val half  = len / 2
            val angle = -2.0 * PI / len
            val wR    = cos(angle).toFloat()
            val wI    = sin(angle).toFloat()
            var i = 0
            while (i < n) {
                var cR = 1f; var cI = 0f
                for (k in 0 until half) {
                    val uR = real[i+k];       val uI = imag[i+k]
                    val vR = real[i+k+half]*cR - imag[i+k+half]*cI
                    val vI = real[i+k+half]*cI + imag[i+k+half]*cR
                    real[i+k]      = uR+vR;  imag[i+k]      = uI+vI
                    real[i+k+half] = uR-vR;  imag[i+k+half] = uI-vI
                    val nR = cR*wR - cI*wI;  cI = cR*wI + cI*wR;  cR = nR
                }
                i += len
            }
            len *= 2
        }
    }

    fun close() { interpreter?.close() }
}

data class AudioResult(val label: String, val confidence: Float)
