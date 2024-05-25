package com.android.whichtowear.retro.repository

import android.location.Location
import android.util.Log
import com.android.whichtowear.retro.service.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.lang.Thread.sleep
import javax.inject.Inject
import kotlin.concurrent.thread

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherService
) {
    @Volatile var res: String = "Unknown"
    suspend fun getWeather(location: Location): String {
        thread {
            val apiKey = "b7670b355ce71c6e52afaa8ba3573807"
            val result: Call<ResponseBody> = weatherApi.getValue(location.latitude,location.longitude, apiKey)
            val response: Response<ResponseBody> = result.execute()
            res = response.body()?.string()!!
        }
        return res
    }
}