package com.android.whichtowear.db.Impl

import com.android.whichtowear.db.dao.ClothingDao
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.db.repository.DetailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val clothingDao: ClothingDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): DetailRepository {
    override suspend fun get(id: Int) = withContext(dispatcher){
        clothingDao.GetWithId(id)
    }

    override suspend fun deleteClothing(id: Int) = withContext(dispatcher){
        clothingDao.DeleteWithId(id)
    }
}