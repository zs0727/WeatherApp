package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.components.CityInputField
import com.example.weatherapp.ui.screens.HistoryScreen
import com.example.weatherapp.ui.screens.WeatherScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import com.example.weatherapp.ui.forecast.ForecastSection
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化 Repository
        val dao = WeatherDatabase.getDatabase(applicationContext).historyDao()
        val repository = WeatherRepository(dao)
        val viewModel: WeatherViewModel by viewModels {
            WeatherViewModelFactory(repository)
        }

        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainContent(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainContent(viewModel: WeatherViewModel) {
    var showHistory by remember { mutableStateOf(false) }
    val forecasts = viewModel.forecast.value

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "天气查询") },
            actions = {
                Text(
                    text = if (showHistory) "查询" else "历史",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { showHistory = !showHistory },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        )
        Text(
            text = "未来天气预报",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )
        if (showHistory) {
            // 历史记录页
            HistoryScreen(viewModel = viewModel)
        } else {
            // 实时查询页
            ForecastSection(forecasts)
            WeatherScreen(viewModel = viewModel)
        }
    }
}
