package com.android.whichtowear.util

import android.annotation.SuppressLint
import java.util.Calendar

fun getDefaultDateInMillis(): Long {
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val date = cal.get(Calendar.DATE)
    cal.clear()
    cal.set(year, month, date)
    return cal.timeInMillis
}

@SuppressLint("SimpleDateFormat")
fun getDateFormat(date: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(date)
}