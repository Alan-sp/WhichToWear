package com.android.whichtowear.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.whichtowear.db.entity.Clothing
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothingDao {
    @Query("Select * From clothing")
    fun GetAllClothing(): Flow<List<Clothing>>

    @Query("Select * From clothing Where id =:id")
    suspend fun GetWithId(id: Int): Clothing

    @Query("Select * From clothing Where id in (:ids)")
    suspend fun GetWithIds(ids: List<Int>):List<Clothing>

    @Query("Delete From Clothing Where id =:id")
    suspend fun DeleteWithId(id:Int):Int

    @Insert
    suspend fun InsertAll(vararg clothings: Clothing)
}