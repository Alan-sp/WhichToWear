package com.android.whichtowear.db.repository

import com.android.whichtowear.db.entity.Clothing
import kotlinx.coroutines.flow.Flow

interface ClothingRepository{
    fun GetAll(): Flow<List<Clothing>>
    suspend fun InsertAll(clothingList: List<Clothing>)
    suspend fun Get(id: Int): Clothing
    suspend fun DeleteClothing(id: Int): Int
}

