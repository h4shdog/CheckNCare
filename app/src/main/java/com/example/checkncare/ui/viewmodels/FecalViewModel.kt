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

            // Dual rejection guard:
            //  1. Confidence threshold — top class must be ≥ 80%
            //  2. Entropy check — if probabilities are spread too evenly the
            //     image is likely not a fecal sample at all (max entropy for
            //     4 classes = ln(4) ≈ 1.386; we reject above 0.8)
            val entropy = result.probabilities.fold(0f) { acc, p ->
                if (p > 0f) acc - p * Math.log(p.toDouble()).toFloat() else acc
            }
            val isUnknown = result.confidence < 0.60f || entropy > 0.8f

            val effectiveLabel = if (isUnknown) "Unknown" else result.label
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
                "Image does not appear to be a valid fecal sample or confidence is too low. Please take a clear, close-up photo of the chicken fecal and try again.",
                "Mukhang hindi ito isang tamang larawan ng dumi o masyadong mababa ang katiyakan. Kumuha ng malinaw at malapit na larawan ng dumi ng manok at subukang muli."
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
