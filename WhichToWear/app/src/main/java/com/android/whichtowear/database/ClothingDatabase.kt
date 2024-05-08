package com.android.whichtowear.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.whichtowear.dao.ClothingDao
import com.android.whichtowear.entity.Clothing

@Database(
    entities = [Clothing::class],
    version = 1
)
abstract class ClothingDatabase:RoomDatabase() {
    abstract fun clothingDao(): ClothingDao
}