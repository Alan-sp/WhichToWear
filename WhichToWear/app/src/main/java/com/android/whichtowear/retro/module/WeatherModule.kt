package com.android.whichtowear.retro.module

import com.android.whichtowear.retro.repository.WeatherRepository
import com.android.whichtowear.retro.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Singleton
    @Provides
    fun provideWeatherService(): WeatherService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(api: WeatherService): WeatherRepository {
        return WeatherRepository(api)
    }
}