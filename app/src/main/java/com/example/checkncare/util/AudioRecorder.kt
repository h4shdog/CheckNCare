package com.example.checkncare.util

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioRecorder {
    private var audioRecord: AudioRecord? = null
    private val sampleRate = 16000  // must match the sample rate used during Colab training
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = maxOf(
        AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat),
        sampleRate * 2  // at least 1 second buffer
    )

    val recordingSampleRate: Int get() = sampleRate

    @SuppressLint("MissingPermission")
    fun startRecording() {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )
        audioRecord?.startRecording()
    }

    suspend fun stopAndGetAudio(): ShortArray = withContext(Dispatchers.IO) {
        // Collect ~3 seconds of audio
        val durationSeconds = 3
        val totalSamples = sampleRate * durationSeconds
        val audioData = ShortArray(totalSamples)
        var readTotal = 0
        while (readTotal < totalSamples) {
            val read = audioRecord?.read(audioData, readTotal, totalSamples - readTotal) ?: 0
            if (read <= 0) break
            readTotal += read
        }
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
        audioData
    }
}
