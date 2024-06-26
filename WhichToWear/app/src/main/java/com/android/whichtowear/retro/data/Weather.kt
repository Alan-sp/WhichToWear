package com.android.whichtowear.retro.data

data class Weather(
    val temp:Double,
    val info:String,
    val main:String,
    val country:String,
    val wet:Int,
    val wind: Double,
    val icon:String,
)
{
    companion object { 
        fun empty() = Weather(0.0, "", "", "",0, 0.0,"Snow")
    }
}