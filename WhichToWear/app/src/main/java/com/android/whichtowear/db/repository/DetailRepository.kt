package com.android.whichtowear.db.repository

import com.android.whichtowear.db.entity.Clothing
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    suspend fun get(id: Int): Clothing

    //fun getLaundry(clothingId: Int): Flow<List<Laundry>>
    //suspend fun addLaundry(id: Int)
    suspend fun deleteClothing(id: Int): Int
}