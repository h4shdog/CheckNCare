package com.example.checkncare.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_history")
data class PredictionRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val time: String,
    val type: String, // "Audio" or "Fecal"
    val result: String,
    val confidence: Float,
    val recommendation: String
)
