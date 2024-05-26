package com.android.whichtowear.db.Impl

import com.android.whichtowear.db.dao.ClothingDao
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.db.repository.ClothingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ClothingRepositoryImpl @Inject constructor(
    private val dao: ClothingDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ClothingRepository {

    override fun GetAll()=dao.GetAllClothing()

    override suspend fun InsertAll(clothingList: List<Clothing>){
        withContext(dispatcher){
            dao.InsertAll(*clothingList.toTypedArray())
        }
    }

    override suspend fun Get(id: Int)=
        withContext(dispatcher){
            dao.GetWithId(id)
        }

    override suspend fun DeleteClothing(id:Int)=
        withContext(Dispatchers.IO){
            dao.DeleteWithId(id)
        }

}
