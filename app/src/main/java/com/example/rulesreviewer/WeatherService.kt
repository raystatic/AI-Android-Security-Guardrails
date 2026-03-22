package com.example.rulesreviewer

import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class WeatherService {
    private val client = OkHttpClient()

    fun fetchWeather(city: String, callback: (String?) -> Unit) {
        val apiKey = BuildConfig.OPENWEATHER_API_KEY
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    callback(response.body?.string())
                } else {
                    callback(null)
                }
            }
        })
    }
}
