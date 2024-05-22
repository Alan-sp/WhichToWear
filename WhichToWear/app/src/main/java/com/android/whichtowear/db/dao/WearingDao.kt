package com.android.whichtowear.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.whichtowear.db.entity.Wearing
import kotlinx.coroutines.flow.Flow

@Dao
interface WearingDao {
    @Query("SELECT * FROM wearing")
    fun GetAll(): Flow<List<Wearing>>

    @Query("SELECT * FROM wearing WHERE id=:id")
    suspend fun Get(id:Int): Wearing

    @Insert
    suspend fun InsertAll(vararg wearingEntity: Wearing)
}