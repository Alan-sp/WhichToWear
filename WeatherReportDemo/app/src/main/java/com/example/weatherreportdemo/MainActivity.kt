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
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import java.util.*

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
                    LocationScreen()
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun LocationScreen() {
    val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }
    var cityName by remember { mutableStateOf<String?>(null) }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val locationListener = object : LocationListener {
        override fun onLocationChanged(loc: Location) {
            location = loc
            cityName = getLocationCityName(context,loc)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    LaunchedEffect(locationManager) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return@LaunchedEffect
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000, // 5 seconds
            10f, // 10 meters
            locationListener
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Current City: ${cityName ?: "Unknown"}")
        Button(onClick = {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            cityName = getLocationCityName(context,location)
        }) {
            Text(text = "Update City")
        }
    }
}

@SuppressLint("MissingPermission")
private fun getLocationCityName(context: Context,location: Location?): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    location?.let {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (addresses!!.isNotEmpty()) {
            return addresses[0].locality
        }
    }
    return "Unknown" + "${location?.latitude},${location?.longitude}"
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
    }
}