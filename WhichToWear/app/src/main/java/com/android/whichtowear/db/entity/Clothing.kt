package com.android.whichtowear.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clothing(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "wear_limit") val wearLimit: Int = 2,
    @ColumnInfo(name = "warmth") val warmth: Float = 0.0f,
    @ColumnInfo(name = "type") val type: Int = 1,
    //@ColumnInfo(name = "color") val color: ColorEnvelope = ColorEnvelope(Color.White,"#FFFFFF",false),
){
    constructor(image: String,
                warmth: Float = 0.0f,
                type: Int,
    ):this(id = 0, image,
        warmth = warmth,
        type = type)
}