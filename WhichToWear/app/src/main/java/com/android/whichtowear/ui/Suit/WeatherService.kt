package com.android.whichtowear.ui.Suit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    companion object {
        //要访问的RESTful Service的基地址
        //注意URL的值: 总是以/结尾， 不要以/开头
        var baseUrl = "https://api.openweathermap.org/data/2.5/"
        val service: WeatherService by lazy {
            //实例化Retrofit
            val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                //使用Gson转换器，将json字符串反序列化为JavaBean或反之
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            //构建用于访问REST服务的retrofit实例
            retrofit.create(WeatherService::class.java)
        }
    }

    //自动反序列化为Java对象
    @GET("weather")
    fun getValue(@Query("q") city:String, @Query("appid") apiKey:String): Call<ResponseBody>
}