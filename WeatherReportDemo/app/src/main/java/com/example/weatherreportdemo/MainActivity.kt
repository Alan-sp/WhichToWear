package com.example.weatherreportdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherreportdemo.ui.theme.WeatherReportDemoTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import WeatherService
import retrofit2.Call
import kotlin.concurrent.thread
import com.example.weatherreportdemo.Weather
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherReportDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun WeatherApp() {
    var city by remember { mutableStateOf("") }
    var weatherInfo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter city") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    // When user presses Done button on keyboard
                    // Fetch weather info
                    fetchWeather(city, callback = { weatherInfo = it })
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(onClick = { fetchWeather(city, callback = { weatherInfo = it }) }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = weatherInfo,
            fontSize = 20.sp,
        )
    }
}

fun fetchWeather(city: String, callback: (String) -> Unit) {

    var service: WeatherService = WeatherService.service
    thread{
        val apiKey = "b7670b355ce71c6e52afaa8ba3573807"
        val result: Call<ResponseBody> = service.getValue(city,apiKey)
        val response: Response<ResponseBody> = result.execute()
        val responseBody = response.body()?.string()
        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json = JSONObject(responseBody)
            val weather = json.getJSONArray("weather").getJSONObject(0).getString("description")
            val temperature = json.getJSONObject("main").getDouble("temp")
            val country = json.getJSONObject("sys").getString("country")

            val weatherInfo = "City: $city, Country: $country\nWeather: $weather, Temperature: ${temperature-273.15}°C"
            callback(weatherInfo)
        } else{
            callback("Error: Unable to fetch weather information.")
        }
//        val apiKey = "b7670b355ce71c6e52afaa8ba3573807" // Replace with your OpenWeatherMap API key
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey")
//            .build()
//
//        runBlocking {
//            val response = client.newCall(request).execute()
//            val responseBody = response.body()?.string()
//            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
//                val json = JSONObject(responseBody)
//                val weather = json.getJSONArray("weather").getJSONObject(0).getString("description")
//                val temperature = json.getJSONObject("main").getDouble("temp")
//                val city = json.getString("name")
//                val country = json.getJSONObject("sys").getString("country")
//
//                val weatherInfo = "City: $city, Country: $country\nWeather: $weather, Temperature: ${temperature-273.15}°C"
//                callback(weatherInfo)
//            } else {
//                callback("Error: Unable to fetch weather information.")
//            }
//        }
    }
}