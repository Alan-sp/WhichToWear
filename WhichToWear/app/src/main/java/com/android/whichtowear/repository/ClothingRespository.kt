package com.android.whichtowear.repository

import com.android.whichtowear.entity.Clothing
import kotlinx.coroutines.flow.Flow

interface ClothingRespository{
    fun GetAll(): Flow<List<Clothing>>
    suspend fun InsertAll(clothingList: List<Clothing>)
    suspend fun Get(id: Int): Clothing

    suspend fun DeleteClothing(id: Int): Int
}

