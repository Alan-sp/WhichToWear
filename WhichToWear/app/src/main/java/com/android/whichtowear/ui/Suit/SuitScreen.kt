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
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.app.ActivityCompat
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.retro.data.Weather
import com.android.whichtowear.ui.Closet.ClosetUiState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import java.util.*


//fun getLocationCityName(context: Context, location: Location): String? {
//    val geocoder = Geocoder(context, Locale.getDefault())
//    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//    return addresses[0].locality
//}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun SuitScreen(
    uiState: ClosetUiState,
    allState: ClosetUiState,
    weatherState: Weather,
    changeWeatherState:(Location) -> Unit,
    updatePhotos: (List<Clothing>) -> Unit,
    addToWearings: (List<Clothing>) -> Unit,
    navigate: (String) -> Unit
) {

    val context = LocalContext.current
    var isin by remember { mutableStateOf(0) }
    var location by remember { mutableStateOf<Location?>(null) }
    var cityName by remember { mutableStateOf<String?>(null) }
    var isPressed by remember { mutableStateOf(false) }
    var isSport by remember { mutableStateOf(false) }
    var isMeet by remember { mutableStateOf(false) }
    var isColor by remember { mutableStateOf(false) }

    val resID = context.resources.getIdentifier(weatherState.icon, "drawable", "com.android.whichtowear")
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val locationListener = object : LocationListener {
        override fun onLocationChanged(loc: Location) {
            location = loc
//            cityName = getLocationCityName(context,loc)
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

    if(weatherState == Weather.empty()  &&
            ActivityCompat.checkSelfPermission(
                    context,
        Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        ) {
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//                    cityName = g
//    Shirt =etLocationCityName(context, location) ?: "Unknown"
//                    Log.d("CityName", "${location!!.latitude}")
        location?.let { it1 -> changeWeatherState(it1) }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "今日穿搭") })
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 30.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            Color.White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(weatherState != Weather.empty()) {
                    Column(modifier = Modifier.padding(vertical = 16.dp)) {
                        Column {
                            //                        Box(modifier = Modifier.size(16.dp))
                            Text(
                                text = weatherState.info + "   ${(weatherState.temp - 273.15).toInt()} ℃",
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(align = Alignment.CenterVertically)  //设置竖直居中
                                    .wrapContentWidth(align = Alignment.CenterHorizontally) //设置水平居中
                            )
                            Image(
                                painter = painterResource(id = resID),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(align = Alignment.CenterVertically)  //设置竖直居中
                                    .wrapContentWidth(align = Alignment.CenterHorizontally) //设置水平居中
                            )
                            Text(
                                text = "湿度：${weatherState.wet}%   风速：${weatherState.wind} m/s",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(align = Alignment.CenterVertically)  //设置竖直居中
                                    .wrapContentWidth(align = Alignment.CenterHorizontally) //设置水平居中
                            )
                        }
                        Box(modifier = Modifier.size(16.dp))
                    }
                }
                else
                {
                    Text(
                        text = if(isin < 10) "天气获取中……请稍候"
                        else "获取失败，请检查网络重试",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically)  //设置竖直居中
                            .wrapContentWidth(align = Alignment.CenterHorizontally) //设置水平居中
                    )
                }

                Text(
                    text = "今日活动：",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.CenterStart),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
//                    layoutDirection = LayoutDirection.Ltr,
                    fontSize = 20.sp,
                )
                CheckboxRow(text = "今日是否有运动安排",
                    selected = isSport,
                    onOptionSelected = { isSport = !isSport })
                CheckboxRow(text = "今日是否参加正式场合",
                    selected = isMeet,
                    onOptionSelected = { isMeet = !isMeet })
                CheckboxRow(text = "今日是否有社交活动",
                    selected = isColor,
                    onOptionSelected = { isColor = !isColor })
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(4.dp)
//                ) {
//                }
                if(!isPressed)
                {
                    Button(onClick = {
                        updatePhotos(selectPhotos(
                            allState as ClosetUiState.PhotoList,
                            weatherState,
                            isSport,
                            isMeet,
                            isColor
                        ))
                        isPressed = true
                    }
                    ) {
                        Text("生成今日穿搭")
                    }
                }
                else
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Button(onClick = {
                            updatePhotos(selectPhotos(
                                allState as ClosetUiState.PhotoList,
                                weatherState,
                                isSport,
                                isMeet,
                                isColor
                            ))
                            isPressed = true
                        }) {
                            Text("重新生成")
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        Button(onClick = {
                            if(uiState is ClosetUiState.PhotoList) {
                                addToWearings(uiState.photos)
                            }
                        }) {
                            Text("添加至今日穿搭")
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    if(uiState is ClosetUiState.PhotoList) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            content = {
                                itemsIndexed(uiState.photos) { index, photo ->
                                    GlideImage(
                                        model = photo.image,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .aspectRatio(1f / 1.6f)
                                            .clip(shape = RoundedCornerShape(4.dp))
                                            .clickable {
                                                navigate("detail/${photo.id}")
                                            }
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CheckboxRow(
    text: String,
    selected: Boolean,
    onOptionSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onOptionSelected)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            Box(Modifier.padding(5.dp)) {
                Checkbox(selected, onCheckedChange = null)
            }
        }
    }
}

fun selectPhotos(
    uiState: ClosetUiState.PhotoList,
    weatherState: Weather,
    isSport: Boolean,
    isMeet: Boolean,
    isColor: Boolean
):List<Clothing>
{
    val resList = mutableListOf<Clothing>()
    var Shirt = Clothing.empty()
    var Pants = Clothing.empty()
    var Shoes = Clothing.empty()
    var shirtList = mutableListOf<Clothing>()
    var pantsList = mutableListOf<Clothing>()
    var shoesList = mutableListOf<Clothing>()
    if(isSport)
    {
        shirtList.addAll((uiState.photos.filter { it.type == 0 && (it.points.and(1) == 1) }).toMutableList())
        pantsList.addAll((uiState.photos.filter { it.type == 1 && (it.points.and(1) == 1) }).toMutableList())
        shoesList.addAll((uiState.photos.filter { it.type == 2 && (it.points.and(1) == 1) }).toMutableList())
    }
    if(isMeet)
    {
        shirtList.addAll((uiState.photos.filter { it.type == 0 && (it.points.and(64) == 64) }).toMutableList())
        pantsList.addAll((uiState.photos.filter { it.type == 1 && (it.points.and(64) == 64) }).toMutableList())
        shoesList.addAll((uiState.photos.filter { it.type == 2 && (it.points.and(64) == 64) }).toMutableList())
        if(Shirt == Clothing.empty())
            shirtList.addAll(uiState.photos.filter { it.type == 0 && (it.points.and(32) == 32)}.toMutableList())
        if(Pants == Clothing.empty())
            pantsList.addAll(uiState.photos.filter { it.type == 1 && (it.points.and(32) == 32)}.toMutableList())
        if(Shoes == Clothing.empty())
            shoesList.addAll(uiState.photos.filter { it.type == 2 && (it.points.and(32) == 32)}.toMutableList())
    }
    if(isColor)
    {
        shirtList.addAll((uiState.photos.filter { it.type == 0 && (it.points.and(16) == 16) }).toMutableList())
        pantsList.addAll((uiState.photos.filter { it.type == 1 && (it.points.and(16) == 16) }).toMutableList())
        shoesList.addAll((uiState.photos.filter { it.type == 2 && (it.points.and(16) == 16) }).toMutableList())
    }
    shirtList.addAll((uiState.photos.filter { it.type == 0 && weatherState.temp + it.warmth >= 30 }).toMutableList())
    pantsList.addAll((uiState.photos.filter { it.type == 1 && weatherState.temp + it.warmth >= 30}).toMutableList())
    shoesList.addAll((uiState.photos.filter { it.type == 2 && weatherState.temp + it.warmth >= 30}).toMutableList())
    if(shirtList.isEmpty())
    {
        shirtList.add(Clothing.empty())
        shirtList.addAll((uiState.photos.filter{ it.type == 0 }.toMutableList()))
    }
    if(pantsList.isEmpty())
    {
        pantsList.add(Clothing.empty())
        pantsList.addAll((uiState.photos.filter{ it.type == 1 }.toMutableList()))
    }
    if(shoesList.isEmpty())
    {
        shoesList.add(Clothing.empty())
        shoesList.addAll((uiState.photos.filter{ it.type == 2 }.toMutableList()))
    }
    Shirt = shirtList.random()
    Pants = pantsList.random()
    Shoes = shoesList.random()
    if(Shirt != Clothing.empty()) resList.add(Shirt)
    if(Pants != Clothing.empty()) resList.add(Pants)
    if(Shoes != Clothing.empty()) resList.add(Shoes)
    return resList
}