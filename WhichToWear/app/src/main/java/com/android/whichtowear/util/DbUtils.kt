package com.android.whichtowear.util

import android.net.Uri
import android.text.format.DateUtils
import android.util.Log
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.db.entity.Wearing
import com.android.whichtowear.retro.data.Weather
import com.android.whichtowear.ui.Closet.ClosetUiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


fun List<Wearing>.groupByTimestamp() = run {
    this.groupBy {
        Date(it.timestamp).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
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
    runBlocking {
        launch {
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
            if(weatherState.main == "Rain")
            {
                for(i in 0..10)
                {
                    shirtList.addAll((uiState.photos.filter { it.type == 0 && (it.points.and(2) == 2) }).toMutableList())
                    pantsList.addAll((uiState.photos.filter { it.type == 1 && (it.points.and(2) == 2) }).toMutableList())
                    shoesList.addAll((uiState.photos.filter { it.type == 2 && (it.points.and(2) == 2) }).toMutableList())
                }
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
        }
    }
    return resList

}

fun getFormattedDateFromLocalDate(localDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return formatter.format(localDate)
}

fun hashList(list: List<Int>): Int {
    var result = 0
    for (element in list) {
        result += (1.shl(element))
    }
    return result
}