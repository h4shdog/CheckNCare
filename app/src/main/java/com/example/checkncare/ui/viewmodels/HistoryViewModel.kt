package com.example.checkncare.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkncare.data.AppDatabase
import com.example.checkncare.data.PredictionRecord
import com.example.checkncare.data.PredictionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PredictionRepository

    // Holds the current search query; empty string = show all
    private val _searchQuery = MutableStateFlow("")

    // All records from DB, always up-to-date
    private val _allRecords: Flow<List<PredictionRecord>>

    /**
     * Emits the filtered list whenever either the query or the full record list changes.
     * Filtering is done in-memory so that:
     *  - date/time searches work ("Jul", "2025", "03:00 PM", etc.)
     *  - Tagalog type/result aliases work ("Tilaok", "Dumi", "May Sakit", etc.)
     *  - English terms always work too
     */
    val records: Flow<List<PredictionRecord>>

    init {
        val dao = AppDatabase.getDatabase(application).predictionDao()
        repository = PredictionRepository(dao)
        _allRecords = repository.allRecords

        records = combine(
            _allRecords,
            _searchQuery.debounce(200)   // wait 200 ms after user stops typing
        ) { list, query ->
            if (query.isBlank()) {
                list
            } else {
                val q = query.trim().lowercase()

                // Normalize full month names to 3-letter abbreviations so
                // "July 06, 2026" matches the stored "Jul 06, 2026"
                val monthMap = mapOf(
                    "january" to "jan", "february" to "feb", "march"     to "mar",
                    "april"   to "apr", "may"      to "may", "june"      to "jun",
                    "july"    to "jul", "august"   to "aug", "september" to "sep",
                    "october" to "oct", "november" to "nov", "december"  to "dec"
                )
                val normalizedQ = monthMap.entries.fold(q) { acc, (full, abbr) ->
                    acc.replace(full, abbr)
                }

                list.filter { record ->
                    // Build all searchable text for this record (English + Tagalog aliases)
                    val searchableTokens = listOf(
                        record.date,
                        record.time,
                        record.result,
                        record.type,
                        // Tagalog type aliases
                        if (record.type == "Audio") "tilaok" else "dumi",
                        // Tagalog result aliases
                        when (record.result) {
                            "Sick"    -> "may sakit"
                            "Unknown" -> "hindi natukoy"
                            else      -> ""
                        }
                    )
                    // Match against both the original query and the normalized one
                    searchableTokens.any { token ->
                        val t = token.lowercase()
                        t.contains(q) || t.contains(normalizedQ)
                    }
                }
            }
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
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
