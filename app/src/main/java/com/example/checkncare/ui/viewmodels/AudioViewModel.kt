package com.example.checkncare.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkncare.data.AppDatabase
import com.example.checkncare.data.PredictionRecord
import com.example.checkncare.data.PredictionRepository
import com.example.checkncare.tflite.AudioClassifier
import com.example.checkncare.util.AudioRecorder
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class AudioUiState(
    val isRecording: Boolean = false,
    val isAnalyzing: Boolean = false,
    val result: AudioPredictionResult? = null,
    val recordingTooShort: Boolean = false,
    val recordingElapsedSeconds: Int = 0
)

data class AudioPredictionResult(
    val label: String,
    val confidence: Float,
    val recommendationEn: String,
    val recommendationTl: String
)

class AudioViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(AudioUiState())
    val state: StateFlow<AudioUiState> = _state.asStateFlow()

    private val recorder = AudioRecorder()
    private val classifier = AudioClassifier(application)
    private val repository: PredictionRepository
    private var recordingStartTime: Long = 0L
    private var timerJob: Job? = null

    init {
        val dao = AppDatabase.getDatabase(application).predictionDao()
        repository = PredictionRepository(dao)
    }

    fun startRecording() {
        recordingStartTime = System.currentTimeMillis()
        _state.value = _state.value.copy(
            isRecording = true,
            result = null,
            recordingTooShort = false,
            recordingElapsedSeconds = 0
        )
        recorder.startRecording()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1_000L)
                val elapsed = ((System.currentTimeMillis() - recordingStartTime) / 1_000L).toInt()
                _state.value = _state.value.copy(recordingElapsedSeconds = elapsed)
            }
        }
    }

    fun stopRecording() {
        timerJob?.cancel()
        timerJob = null
        val elapsedMs = System.currentTimeMillis() - recordingStartTime
        if (elapsedMs < 5_000L) {
            // Recording is too short — stop the recorder but don't analyze
            viewModelScope.launch {
                recorder.stopAndGetAudio() // drain & release the AudioRecord
            }
            _state.value = _state.value.copy(isRecording = false, isAnalyzing = false, recordingTooShort = true)
            return
        }

        viewModelScope.launch {
            val audioData = recorder.stopAndGetAudio()
            _state.value = _state.value.copy(isRecording = false, isAnalyzing = true)

            val result = classifier.classify(audioData)

            // If confidence is below 60%, treat it as Unknown and skip saving to history
            val effectiveLabel = if (result.confidence < 0.50f) "Unknown" else result.label
            val (recEn, recTl) = getRecommendations(effectiveLabel)

            val predictionResult = AudioPredictionResult(
                label            = effectiveLabel,
                confidence       = result.confidence,
                recommendationEn = recEn,
                recommendationTl = recTl
            )

            _state.value = _state.value.copy(isAnalyzing = false, result = predictionResult)

            saveToHistory(predictionResult)
        }
    }

    private fun getRecommendations(label: String): Pair<String, String> {
        return when (label) {
            "Normal" -> Pair(
                "Chicken vocalization appears normal. Continue routine monitoring.",
                "Normal ang tunog ng manok. Ipagpatuloy ang regular na pagmamasid."
            )
            "Sick" -> Pair(
                "Possible respiratory disease detected. Monitor the chicken and consult a veterinarian.",
                "May posibleng sakit sa paghinga. Bantayan ang manok at kumonsulta sa beterinaryo."
            )
            else -> Pair(
                "Unable to determine condition — confidence is below 60%. Please record again in a quieter environment.",
                "Hindi matukoy ang kondisyon — ang katiyakan ay mas mababa sa 60%. Subukang mag-record muli sa mas tahimik na lugar."
            )
        }
    }

    private suspend fun saveToHistory(result: AudioPredictionResult) {
        val sdfDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val sdfTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val record = PredictionRecord(
            date = sdfDate.format(Date()),
            time = sdfTime.format(Date()),
            type = "Audio",
            result = result.label,
            confidence = result.confidence,
            recommendation = result.recommendationEn
        )
        repository.insert(record)
    }

    override fun onCleared() {
        super.onCleared()
        classifier.close()
    }
}
