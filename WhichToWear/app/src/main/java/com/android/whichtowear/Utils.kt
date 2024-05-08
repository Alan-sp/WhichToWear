package com.android.whichtowear

import android.net.Uri
import android.text.format.DateUtils
import com.android.whichtowear.entity.Clothing
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun List<Uri>.toClothingList() = run {
    map {
        Clothing(image = it.toString())
    }
}

//fun List<Outfit>.groupByTimestamp() = run {
//    this.groupBy {
//        Date(it.timestamp).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//    }
//}

fun getDateFromTimeStamp(timestamp: Long): String {
    return DateUtils.getRelativeTimeSpanString(
        timestamp,
        System.currentTimeMillis(),
        DateUtils.DAY_IN_MILLIS
    ).toString()
}

fun getFormattedDateFromLocalDate(localDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return formatter.format(localDate)
}