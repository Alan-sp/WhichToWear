package com.android.whichtowear.retro.repository

import com.android.whichtowear.retro.service.WeatherService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherService
) {
    suspend fun getWeather(city: String): String? {
        val apiKey = "b7670b355ce71c6e52afaa8ba3573807"
        val result: Call<ResponseBody> = weatherApi.getValue(city, apiKey)
        val response: Response<ResponseBody> = result.execute()
        return response.body()?.string()
    }
}