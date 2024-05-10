package com.android.whichtowear.retro.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    //自动反序列化为Java对象
    @GET("weather")
    fun getValue(@Query("q") city:String, @Query("appid") apiKey:String): Call<ResponseBody>
}