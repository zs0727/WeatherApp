// ui/screens/HistoryScreen.kt
package com.example.weatherapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.local.HistoryEntity
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: WeatherViewModel) {
    val historyList = viewModel.historyList.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        if (historyList.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "暂无历史记录", fontSize = 18.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(historyList.value) { item ->
                    HistoryItemCard(item) {
                        // 点击历史记录后，再次查询（直接用缓存或网络请求）
                        viewModel.fetchWeather(item.city)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: HistoryEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = item.city, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "温度：${item.temperature} ℃", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "描述：${item.description}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val timeStr = sdf.format(Date(item.timestamp * 1000))
            Text(text = "查询时间：$timeStr", fontSize = 12.sp)
        }
    }
}
