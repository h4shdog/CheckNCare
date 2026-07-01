package com.example.checkncare.tflite

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream

class FecalClassifier(private val context: Context) {
    private var interpreter: Interpreter? = null
    private val modelPath = "FecalModel.tflite"

    init {
        try {
            interpreter = Interpreter(loadModelFile())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.length
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classify(bitmap: Bitmap): FecalResult {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val byteBuffer = ByteBuffer.allocateDirect(1 * 224 * 224 * 3 * 4)
        byteBuffer.order(ByteOrder.nativeOrder())
        
        val intValues = IntArray(224 * 224)
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.width, 0, 0, resizedBitmap.width, resizedBitmap.height)
        
        var pixel = 0
        for (unusedI in 0 until 224) {
            for (unusedJ in 0 until 224) {
                val value = intValues[pixel++]
                // Send raw [0, 255] float values — the model has a Rescaling layer
                // baked in that does (x / 127.5) - 1 internally. Double-normalizing
                // was causing wrong predictions.
                byteBuffer.putFloat((value shr 16 and 0xFF).toFloat())
                byteBuffer.putFloat((value shr 8 and 0xFF).toFloat())
                byteBuffer.putFloat((value and 0xFF).toFloat())
            }
        }

        val output = Array(1) { FloatArray(4) }
        interpreter?.run(byteBuffer, output)

        val probabilities = output[0]
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val confidence = if (maxIndex != -1) probabilities[maxIndex] else 0f

        val label = when (maxIndex) {
            0 -> "Normal"
            1 -> "Coccidiosis"
            2 -> "Newcastle Disease"
            3 -> "Salmonella Infection"
            else -> "Unknown"
        }

        return FecalResult(label, confidence, probabilities)
    }

    fun close() {
        interpreter?.close()
    }
}

data class FecalResult(val label: String, val confidence: Float, val probabilities: FloatArray)
