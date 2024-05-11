package com.android.whichtowear.ui.Suit

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

//import com.android.whichtowear.retro.data.Weather
@HiltViewModel
class SuitViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel()
{
    private val _weatherState = MutableStateFlow<Weather>(Weather.empty())
    val weatherState : StateFlow<Weather> = _weatherState

    fun changeWeatherState(city:String)
    {
        viewModelScope.launch {
            val webData = repository.getWeather(city)
            if(webData.isNullOrBlank()){
                _weatherState.value = Weather.empty()
                return@launch
            }
            val json = JSONObject(webData)
            val weather = json.getJSONArray("weather").getJSONObject(0).getString("description")
            val temperature = json.getJSONObject("main").getDouble("temp")
            val country = json.getJSONObject("sys").getString("country")
            _weatherState.value = Weather(temperature, weather, city , country, 0)
        }
    }
}