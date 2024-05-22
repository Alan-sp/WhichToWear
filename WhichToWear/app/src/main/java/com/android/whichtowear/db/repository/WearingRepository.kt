package com.android.whichtowear.db.repository

import com.android.whichtowear.db.entity.Wearing
import kotlinx.coroutines.flow.Flow

interface WearingRepository {
    fun GetAllAsMap(): Flow<Map<String,List<Wearing>>>
    suspend fun InsertAll(wearingList: List<Wearing>)
    suspend fun Get(id:Int):Wearing
}