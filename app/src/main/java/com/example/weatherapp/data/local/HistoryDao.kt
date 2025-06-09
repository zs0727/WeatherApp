// data/local/HistoryDao.kt
package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    // 插入一条历史记录；已存在（同一城市、同一时间戳）则替换
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity)

    // 查询某城市最近一次记录
    @Query("SELECT * FROM weather_history WHERE city = :city ORDER BY timestamp DESC LIMIT 1")
    suspend fun fetchLatestByCity(city: String): HistoryEntity?

    // 查询所有历史记录（按时间倒序）
    @Query("SELECT * FROM weather_history ORDER BY timestamp DESC")
    fun fetchAllHistory(): Flow<List<HistoryEntity>>
}
