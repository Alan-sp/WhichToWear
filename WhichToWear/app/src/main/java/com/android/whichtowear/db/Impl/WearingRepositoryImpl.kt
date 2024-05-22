package com.android.whichtowear.db.Impl

import com.android.whichtowear.db.dao.WearingDao
import com.android.whichtowear.db.entity.Wearing
import com.android.whichtowear.db.repository.WearingRepository
import com.android.whichtowear.util.getFormattedDateFromLocalDate
import com.android.whichtowear.util.groupByTimestamp
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class WearingRepositoryImpl @Inject constructor(
    private val wearingDao: WearingDao
):WearingRepository{
    private val currentDate = LocalDate.now()

    override fun GetAllAsMap() = wearingDao.GetAll().map {
        it.groupByTimestamp().toSortedMap()
    }.map {
        it.mapKeys { (localDate, _) ->
            when(localDate){
                currentDate -> "Today"
                currentDate.minusDays(1) -> "Yesterday"
                else -> getFormattedDateFromLocalDate(localDate)
            }
        }
    }

    override suspend fun InsertAll(wearingList: List<Wearing>) {
        wearingDao.InsertAll(*wearingList.toTypedArray())

    }

    override suspend fun Get(id: Int) = wearingDao.Get(id)
}