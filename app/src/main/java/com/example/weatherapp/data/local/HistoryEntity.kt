package com.example.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_history"
)
data class HistoryEntity(
    val id: Long = 0,
    @PrimaryKey val city: String,
    val timestamp: Long,       // Unix 时间戳
    val temperature: Double,   // 摄氏度
    val description: String    // 天气描述
)