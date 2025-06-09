// viewmodel/WeatherViewModel.kt
package com.example.weatherapp.viewmodel
import androidx.compose.runtime.State
import com.example.weatherapp.data.network.WeatherService
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.local.HistoryEntity
import com.example.weatherapp.data.model.ForecastCast
import com.example.weatherapp.data.model.ForecastResponse
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _historyList = MutableStateFlow<List<HistoryEntity>>(emptyList())
    val historyList: StateFlow<List<HistoryEntity>> = _historyList

    init {
        // 预先加载所有历史
        viewModelScope.launch {
            repository.getAllHistory().collect { list ->
                _historyList.value = list
            }
        }
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            //val realtime = WeatherService.fetchRealtime(cityCode)     // 实况
            _errorMessage.value = null
            val result = repository.getWeather(city)
            val forecastResp = WeatherService.fetchWeatherByCityForcast(city)
            forecastResp?.forecasts?.firstOrNull()?.casts?.let { _forecast.value = it }
            if (result != null) {
                _weatherState.value = result
            } else {
                _errorMessage.value = "获取天气失败，请检查网络或城市名"
            }
            _isLoading.value = false
        }
    }
    private val _forecast = mutableStateOf<List<ForecastCast>>(emptyList())
    val forecast: State<List<ForecastCast>> = _forecast

    fun loadForecast(cityCode: String) {
        viewModelScope.launch {
            val response = WeatherService.fetchWeatherByCityForcast(cityCode)
            response?.forecasts?.firstOrNull()?.casts?.let {
                _forecast.value = it
            }
        }
    }
}

// Factory
class WeatherViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
