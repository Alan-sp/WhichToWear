package com.android.whichtowear.ui.Suit

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
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import kotlin.concurrent.thread

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import com.android.whichtowear.retro.data.Weather
import com.bumptech.glide.integration.compose.GlideImage
import java.util.*

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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuitScreen(
    weatherState: Weather,
    changeWeatherState:(Location) -> Unit
) {
    var weatherInfo by remember { mutableStateOf(
        "\"City: ${weatherState.main}, Country: ${weatherState.country}\\nWeather: ${weatherState.info}, Temperature: ${weatherState.temp - 273.15}°C\""
    ) }

    val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }
    var cityName by remember { mutableStateOf<String?>(null) }
    val scope  = rememberCoroutineScope()

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
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "xx市") })
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    Column {
                        Box(modifier = Modifier.size(16.dp))
                        Text(
                            text = "${(weatherState.temp- 273.15).toInt()} Degree",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text =  "weatherState.info",
                        )
//                        Icon(
//                            imageVector = Icons.Filled.Add,
//                        )
                    }
                    Box(modifier = Modifier.size(16.dp))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    cityName = getLocationCityName(context, location) ?: "Unknown"
//                    Log.d("CityName", "${location!!.latitude}")
                    location?.let { it1 -> changeWeatherState(it1) }
//                    fetchWeather(cityName!!) {
//                        weatherInfo = it
//                    }
                }
                ) {
                    Text("Get Weather")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "\"City: ${weatherState.main}, Country: ${weatherState.country}\\nWeather: ${weatherState.info}, Temperature: ${weatherState.temp - 273.15}°C\"",
//                    text = weatherInfo,
                    fontSize = 20.sp,
                )
            }
        }
    }
}