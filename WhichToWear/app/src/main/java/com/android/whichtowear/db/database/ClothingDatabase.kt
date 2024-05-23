package com.android.whichtowear.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.whichtowear.db.dao.ClothingDao
import com.android.whichtowear.db.dao.WearingDao
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.db.entity.Wearing

@Database(
    entities = [Clothing::class, Wearing::class,],
    version = 3
)
abstract class ClothingDatabase:RoomDatabase() {
    abstract fun clothingDao(): ClothingDao
    abstract fun wearingDao():WearingDao
}