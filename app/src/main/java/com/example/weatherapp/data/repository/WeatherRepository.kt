// data/repository/WeatherRepository.kt
package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.HistoryDao
import com.example.weatherapp.data.local.HistoryEntity
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.WeatherService
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private val historyDao: HistoryDao) {
    /**
     * 获取指定城市天气信息：
     * 1. 先查询本地历史记录（最新的一条）。如果记录存在且时间戳与当前“小时”一致（例如：同一天内，或可加入自定义有效期），则直接返回本地缓存；
     * 2. 否则发起网络请求获取最新天气，并插入到本地。
     */
    private val CACHE_VALIDITY_SECONDS = 3600L
    suspend fun getWeather(city: String): WeatherResponse? {
        val local = historyDao.fetchLatestByCity(city)
        val currentTime = System.currentTimeMillis() / 1000  // 秒级时间戳
        // 这里可以自定义判断：如果 local 不为空，且 (currentTime - local.timestamp) < 1 小时（3600 秒），就直接返回本地
        if (local != null && (currentTime - local.timestamp) < 3600) {
            // 如果只想返回本地，则需要把 HistoryEntity 转为 WeatherResponse
            return local.toWeatherResponse()
        }
        // 否则发网络请求
        val response = WeatherService.fetchWeatherByCity(city)
        response?.let {
            // 插入到本地
            val history = HistoryEntity(
                city = it.cityName,
                timestamp = it.timestamp,
                temperature = it.mainInfo.temp,
                description = it.weather.firstOrNull()?.description ?: ""
            )
            historyDao.insert(history)
        }
        return response
    }

    fun getAllHistory(): Flow<List<HistoryEntity>> = historyDao.fetchAllHistory()
}

// 扩展函数：把本地存储转为 WeatherResponse（简化版，仅展示最核心字段）
private fun HistoryEntity.toWeatherResponse(): WeatherResponse {
    return WeatherResponse(
        cityName = this.city,
        timestamp = this.timestamp,
        weather = listOf(
            com.example.weatherapp.data.model.WeatherDesc(
                main = "", description = this.description
            )
        ),
        mainInfo = com.example.weatherapp.data.model.MainInfo(
            temp = this.temperature,
            tempMin = this.temperature,
            tempMax = this.temperature,
            humidity = 0
        )
    )
}
