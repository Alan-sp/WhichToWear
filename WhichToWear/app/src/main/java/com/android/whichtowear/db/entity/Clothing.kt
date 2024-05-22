package com.android.whichtowear.db.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.whichtowear.util.getDefaultDateInMillis
import com.github.skydoves.colorpicker.compose.ColorEnvelope

@Entity
data class Clothing(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "wear_limit") val wearLimit: Int = 2,
    @ColumnInfo(name = "warmth") val warmth: Float = 0.0f,
    @ColumnInfo(name = "type") val type: Int = 1,
    @ColumnInfo(name = "date") val date: Long = 0,
    @ColumnInfo(name = "points") val points: Int = 0,
    @ColumnInfo(name = "color") val color : String,
    //@ColumnInfo(name = "color") val color: ColorEnvelope = ColorEnvelope(Color.White,"#FFFFFF",false),
){
    constructor(image: String,
                warmth: Float = 0.0f,
                type: Int,
                date: Long = getDefaultDateInMillis(),
                points: Int,
                color: String
    ):this(id = 0, image,
        warmth = warmth,
        type = type,
        date = date,
        points = points,
        color = color
        )
}