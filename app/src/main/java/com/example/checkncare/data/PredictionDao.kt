package com.example.checkncare.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionDao {
    @Query("SELECT * FROM prediction_history ORDER BY id DESC")
    fun getAllRecords(): Flow<List<PredictionRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(record: PredictionRecord)

    @Delete
    fun deleteRecord(record: PredictionRecord)

    @Query("DELETE FROM prediction_history")
    fun deleteAllRecords()

    @Query("""
        SELECT * FROM prediction_history 
        WHERE result LIKE '%' || :q || '%' 
           OR type   LIKE '%' || :q || '%'
           OR date   LIKE '%' || :q || '%'
           OR time   LIKE '%' || :q || '%'
        ORDER BY id DESC
    """)
    fun searchRecords(q: String): Flow<List<PredictionRecord>>
}
