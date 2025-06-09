package com.example.weatherapp.data.model

data class ForecastResponse(
    val status: String,
    val count: String,
    val info: String,
    val infocode: String,
    val forecasts: List<Forecast>?
)

data class Forecast(
    val city: String,
    val adcode: String,
    val province: String,
    val reporttime: String,
    val casts: List<ForecastCast>?
)

data class ForecastCast(
    val date: String,
    val week: String,
    val dayweather: String,
    val nightweather: String,
    val daytemp: String,
    val nighttemp: String,
    val daywind: String,
    val nightwind: String,
    val daypower: String,
    val nightpower: String
)
