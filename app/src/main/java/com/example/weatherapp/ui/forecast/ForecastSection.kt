package com.example.weatherapp.ui.forecast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.model.ForecastCast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Card
// Material 3 的 CardDefaults
import androidx.compose.material3.CardDefaults
@Composable
fun ForecastSection(forecasts: List<ForecastCast>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(forecasts) { cast ->
            ForecastCard(cast)
        }
    }
}

@Composable
fun ForecastCard(cast: ForecastCast) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(160.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(text = cast.date, fontWeight = FontWeight.Bold)
            Text(text = "星期${cast.week}")
            Text(text = "白天：${cast.dayweather}，${cast.daytemp}°")
            Text(text = "晚上：${cast.nightweather}，${cast.nighttemp}°")
            Text(text = "风力：${cast.daywind} ${cast.daypower}级")
        }
    }
}
