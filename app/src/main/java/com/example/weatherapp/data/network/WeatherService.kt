// data/network/WeatherService.kt
package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.ForecastResponse
import com.example.weatherapp.data.model.MainInfo
import com.example.weatherapp.data.model.WeatherDesc
import com.example.weatherapp.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.Gson

object WeatherService {
    private const val BASE_URL = "https://restapi.amap.com/v3/weather/weatherInfo"
    private const val API_KEY = "4313945ce6381fdd6b4a8ee337ffc130"  // TODO: 替换成你在高德官网申请的 Key

    private val client = OkHttpClient()

    /**
     * 调用高德地图天气实况接口，并映射成我们通用的 WeatherResponse
     *
     * @param city 参数应传入高德的 adcode（如：110101）
     * @return WeatherResponse?  or null on failure
     */
    suspend fun fetchWeatherByCity(city: String): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            // 构造 URL：https://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=xxx&extensions=base&output=JSON
            val url = BASE_URL.toHttpUrlOrNull()?.newBuilder()
                ?.addQueryParameter("city", city)
                ?.addQueryParameter("key", API_KEY)
                ?.addQueryParameter("extensions", "base")
                ?.addQueryParameter("output", "JSON")
                ?.build()
                ?: return@withContext null

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return@withContext null
                val body = resp.body?.string().takeIf { !it.isNullOrBlank() } ?: return@withContext null

                // 用 org.json 解析高德返回的 JSON
                val root = JSONObject(body)
                if (root.optString("status") != "1") {
                    // status=1 才是成功
                    return@withContext null
                }
                val lives = root.getJSONArray("lives")
                if (lives.length() == 0) return@withContext null
                val live = lives.getJSONObject(0)

                // 城市名、天气、温度、湿度、发布时间
                val cityName    = live.optString("city")
                val weatherDesc = live.optString("weather")      // 汉字描述
                val temperature = live.optString("temperature")  // 摄氏度字符串
                val humidity    = live.optString("humidity")     // % 字符串
                val reportTime  = live.optString("reporttime")   //

                // 将 reporttime 转为 Unix 秒
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                sdf.timeZone = TimeZone.getDefault()
                val timestamp = try {
                    sdf.parse(reportTime)?.time?.div(1000) ?: (System.currentTimeMillis() / 1000)
                } catch (e: Exception) {
                    System.currentTimeMillis() / 1000
                }
                System.out.println("原始时间: $reportTime")         // "2025-06-09 11:32:00"
                System.out.println("转换后时间: ${Date(timestamp*1000)}") // 显示错误的时间

                // 构造通用模型
                WeatherResponse(
                    cityName = cityName,
                    timestamp = timestamp,
                    weather = listOf(
                        WeatherDesc(
                            main = weatherDesc,
                            description = weatherDesc
                        )
                    ),
                    mainInfo = MainInfo(
                        temp    = temperature.toDoubleOrNull() ?: 0.0,
                        tempMin = temperature.toDoubleOrNull() ?: 0.0,
                        tempMax = temperature.toDoubleOrNull() ?: 0.0,
                        humidity= humidity.toIntOrNull() ?: 0
                    )
                )
            }
        }
    }


    suspend fun fetchWeatherByCityForcast(cityCode: String): ForecastResponse? {
        val url = "$BASE_URL?city=$cityCode&key=$API_KEY&extensions=all&output=JSON"

        //val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val body = response.body?.string()
                body?.let {
                    val gson = Gson()
                    gson.fromJson(it, ForecastResponse::class.java)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

