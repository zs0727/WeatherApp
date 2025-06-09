// ui/components/CityInputField.kt
package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CityInputField(onSearch: (String) -> Unit) {
    var cityName by remember { mutableStateOf("") }
    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("输入城市") },
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                if (cityName.isNotBlank()) {
                    onSearch(cityName.trim())
                }
            },
            modifier = Modifier.alignByBaseline()
        ) {
            Text("查询")
        }
    }
}
