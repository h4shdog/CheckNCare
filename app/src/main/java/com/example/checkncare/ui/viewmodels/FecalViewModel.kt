package com.example.checkncare.ui.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkncare.data.AppDatabase
import com.example.checkncare.data.PredictionRecord
import com.example.checkncare.data.PredictionRepository
import com.example.checkncare.tflite.FecalClassifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class FecalUiState(
    val selectedImage: Bitmap? = null,
    val isAnalyzing: Boolean = false,
    val result: FecalPredictionResult? = null
)

data class FecalPredictionResult(
    val label: String,
    val confidence: Float,
    val recommendationEn: String,
    val recommendationTl: String
)

class FecalViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(FecalUiState())
    val state: StateFlow<FecalUiState> = _state.asStateFlow()

    private val classifier = FecalClassifier(application)
    private val repository: PredictionRepository

    init {
        val dao = AppDatabase.getDatabase(application).predictionDao()
        repository = PredictionRepository(dao)
    }

    fun onImageSelected(bitmap: Bitmap) {
        _state.value = _state.value.copy(selectedImage = bitmap, result = null)
    }

    fun analyzeImage() {
        val bitmap = _state.value.selectedImage ?: return
        _state.value = _state.value.copy(isAnalyzing = true)

        viewModelScope.launch {
            val result = classifier.classify(bitmap)

            // If confidence is below 60%, treat it as Unknown and skip saving to history
            val effectiveLabel = if (result.confidence < 0.60f) "Unknown" else result.label
            val (recEn, recTl) = getRecommendations(effectiveLabel)

            val predictionResult = FecalPredictionResult(
                label            = effectiveLabel,
                confidence       = result.confidence,
                recommendationEn = recEn,
                recommendationTl = recTl
            )

            _state.value = _state.value.copy(isAnalyzing = false, result = predictionResult)

            if (effectiveLabel != "Unknown") {
                saveToHistory(predictionResult)
            }
        }
    }

    private fun getRecommendations(label: String): Pair<String, String> {
        return when (label) {
            "Normal" -> Pair(
                "Normal fecal condition detected. Continue routine monitoring.",
                "Normal ang dumi ng manok. Ipagpatuloy ang pagmamasid."
            )
            "Coccidiosis" -> Pair(
                "Possible Coccidiosis detected. Consult a veterinarian and monitor the flock.",
                "Posibleng Coccidiosis. Kumonsulta sa beterinaryo at bantayan ang kawan."
            )
            "Newcastle Disease" -> Pair(
                "Possible Newcastle Disease detected. Immediate veterinary consultation is recommended.",
                "Posibleng Newcastle Disease. Inirerekomenda ang agarang konsultasyon sa beterinaryo."
            )
            "Salmonella Infection" -> Pair(
                "Possible Salmonella Infection detected. Observe biosecurity protocols and consult a veterinarian.",
                "Posibleng Salmonella Infection. Sundin ang biosecurity protocols at kumonsulta sa beterinaryo."
            )
            else -> Pair(
                "Unable to determine condition. Please retake a proper fecal image and try again.",
                "Hindi matukoy ang kondisyon. Kumuha muli ng maayos na larawan ng dumi at subukang muli."
            )
        }
    }

    private suspend fun saveToHistory(result: FecalPredictionResult) {
        val sdfDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val sdfTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val record = PredictionRecord(
            date = sdfDate.format(Date()),
            time = sdfTime.format(Date()),
            type = "Fecal",
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
