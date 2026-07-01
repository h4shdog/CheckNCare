package com.example.checkncare.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkncare.data.AppDatabase
import com.example.checkncare.data.PredictionRecord
import com.example.checkncare.data.PredictionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PredictionRepository
    val records: Flow<List<PredictionRecord>>

    init {
        val dao = AppDatabase.getDatabase(application).predictionDao()
        repository = PredictionRepository(dao)
        records = repository.allRecords
    }

    fun search(query: String) {
        // In a real app with more time, I'd use Flow transformation
        // records = repository.search(query)
    }

    fun delete(record: PredictionRecord) {
        viewModelScope.launch {
            repository.delete(record)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}
