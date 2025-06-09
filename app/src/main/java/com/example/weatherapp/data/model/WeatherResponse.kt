package com.example.weatherapp.data.model

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "name") val cityName: String,
    @Json(name = "dt") val timestamp: Long,  // 更新时间（Unix 时间戳）
    @Json(name = "weather") val weather: List<WeatherDesc>,
    @Json(name = "main") val mainInfo: MainInfo
)

data class WeatherDesc(
    @Json(name = "main") val main: String,       // 如 "Clear", "Clouds"
    @Json(name = "description") val description: String // 如 "clear sky"
)

data class MainInfo(
    @Json(name = "temp") val temp: Double,  // 温度（Kelvin）
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    @Json(name = "humidity") val humidity: Int
)