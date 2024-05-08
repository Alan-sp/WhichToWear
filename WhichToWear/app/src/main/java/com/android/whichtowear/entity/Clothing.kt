package com.android.whichtowear.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clothing(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "wear_limit") val wearLimit: Int = 2
){
    constructor(image: String):this(id = 0, image)
}