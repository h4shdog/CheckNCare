package com.example.checkncare.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PredictionRepository(private val predictionDao: PredictionDao) {
    val allRecords: Flow<List<PredictionRecord>> = predictionDao.getAllRecords()

    suspend fun insert(record: PredictionRecord) = withContext(Dispatchers.IO) {
        predictionDao.insertRecord(record)
    }

    suspend fun delete(record: PredictionRecord) = withContext(Dispatchers.IO) {
        predictionDao.deleteRecord(record)
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        predictionDao.deleteAllRecords()
    }

    fun search(query: String): Flow<List<PredictionRecord>> {
        return predictionDao.searchRecords(query)
    }
}
