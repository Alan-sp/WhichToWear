package com.android.whichtowear.ui.Suit

import android.location.Location
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.whichtowear.retro.data.Weather
import com.android.whichtowear.retro.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.concurrent.thread

//import com.android.whichtowear.retro.data.Weather
@HiltViewModel
class SuitViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel()
{
    private val _weatherState = MutableStateFlow<Weather>(Weather.empty())
    val weatherState : StateFlow<Weather> = _weatherState

    fun changeWeatherState(location: Location)
    {
//        thread{
            viewModelScope.launch {
                val webData = repository.getWeather(location)
                if(webData == "Unknown"){
                    _weatherState.value = Weather.empty()
                    return@launch
                }
                val json = JSONObject(webData)
                val weather = json.getJSONArray("weather").getJSONObject(0).getString("description")
                val temperature = json.getJSONObject("main").getDouble("temp")
                val country = json.getJSONObject("sys").getString("country")
                val city = json.getString("name")
                val icon = json.getJSONArray("weather").getJSONObject(0).getString("icon")
                _weatherState.value = Weather(temperature, weather, city , country, 0,icon)
            }
//        }
    }
}