package com.android.whichtowear.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.whichtowear.db.dao.ClothingDao
import com.android.whichtowear.db.entity.Clothing

@Database(
    entities = [Clothing::class],
    version = 1
)
abstract class ClothingDatabase:RoomDatabase() {
    abstract fun clothingDao(): ClothingDao
}