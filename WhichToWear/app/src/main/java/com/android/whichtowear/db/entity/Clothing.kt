package com.android.whichtowear.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.whichtowear.util.getDefaultDateInMillis

@Entity
data class Clothing(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "wear_limit") val wearLimit: Int = 2,
    @ColumnInfo(name = "warmth") val warmth: Float = 0.0f,
    @ColumnInfo(name = "type") val type: Int = 1,
    @ColumnInfo(name = "date") val date: Long = 0,
    @ColumnInfo(name = "points") val points: Int = 0,
    //@ColumnInfo(name = "color") val color: ColorEnvelope = ColorEnvelope(Color.White,"#FFFFFF",false),
){
    constructor(image: String,
                warmth: Float = 0.0f,
                type: Int,
                date: Long = getDefaultDateInMillis(),
                points: Int,
    ):this(id = 0, image,
        warmth = warmth,
        type = type,
        date = date,
        points = points
        )
    companion object{
        fun empty() = Clothing(0, "", 0, 0.0f, 0, 0, 0)
    }
}