// ui/screens/WeatherScreen.kt
package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.components.CityInputField
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherState = viewModel.weatherState.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMsg = viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        CityInputField(onSearch = { city ->
            viewModel.fetchWeather(city)
        })

        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            weatherState.value?.let { data ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = data.cityName, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${data.mainInfo.temp} ℃",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val desc = data.weather.firstOrNull()?.description ?: ""
                    Text(text = desc, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val updatedTime = sdf.format(Date(data.timestamp * 1000))
                    Text(text = "更新时间：$updatedTime", fontSize = 14.sp)
                }
            } ?: run {
                errorMsg.value?.let { msg ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = msg, color = androidx.compose.ui.graphics.Color.Red)
                    }
                }
            }
        }
    }
}
