package com.android.whichtowear.ui.Suit

import android.location.Location
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.db.repository.ClothingRespository
import com.android.whichtowear.retro.data.Weather
import com.android.whichtowear.retro.repository.WeatherRepository
import com.android.whichtowear.ui.Closet.ClosetUiState
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
    private val weatherepository: WeatherRepository,
    private val clothingRespository: ClothingRespository
): ViewModel()
{
    private val _weatherState = MutableStateFlow<Weather>(Weather.empty())
    val weatherState : StateFlow<Weather> = _weatherState

    private val _uiState = MutableStateFlow<ClosetUiState>(ClosetUiState.PhotoList(emptyList()))
    val uiState: StateFlow<ClosetUiState> = _uiState

//    init {
//        viewModelScope.launch {
//            clothingRespository.GetAll().collect {
//                updateState(ClosetUiState.PhotoList(it))
//            }
//        }
//    }
    fun changeWeatherState(location: Location)
    {
//        thread{
            viewModelScope.launch {
                val webData = weatherepository.getWeather(location)
                if(webData == "Unknown"){
                    _weatherState.value = Weather.empty()
                    return@launch
                }
                val json = JSONObject(webData)
                val weather = json.getJSONArray("weather").getJSONObject(0).getString("description")
                val temperature = json.getJSONObject("main").getDouble("temp")
                val country = json.getJSONObject("sys").getString("country")
                val city = json.getString("name")
                val icon = json.getJSONArray("weather").getJSONObject(0).getString("main")
                _weatherState.value = Weather(temperature, weather, city , country, 0,icon)
            }
//        }
    }

    private fun updateState(newState: ClosetUiState) {
        _uiState.value = newState
    }



    fun updatePhotos(photos: List<Clothing>) {
        _uiState.value = ClosetUiState.PhotoList(photos)
    }
}