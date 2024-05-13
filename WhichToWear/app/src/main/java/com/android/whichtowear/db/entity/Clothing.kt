package com.android.whichtowear.db.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.skydoves.colorpicker.compose.ColorEnvelope

@Entity
data class Clothing(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "wear_limit") val wearLimit: Int = 2,
    @ColumnInfo(name = "warmth") val warmth: Int = 0,
    @ColumnInfo(name = "type") val type: String = "shoes",
    //@ColumnInfo(name = "color") val color: ColorEnvelope = ColorEnvelope(Color.White,"#FFFFFF",false),
){
    constructor(image: String):this(id = 0, image)
}