package com.android.whichtowear.db.repository

import com.android.whichtowear.db.entity.Clothing
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    suspend fun get(id: Int): Clothing
    suspend fun deleteClothing(id: Int): Int
}