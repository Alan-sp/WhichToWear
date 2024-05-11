package com.android.whichtowear.db.module

import com.android.whichtowear.db.Impl.ClosetRepositoryImpl
import com.android.whichtowear.db.Impl.DetailRepositoryImpl
import com.android.whichtowear.db.dao.ClothingDao
import com.android.whichtowear.db.repository.ClothingRespository
import com.android.whichtowear.db.repository.DetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.annotation.Signed
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideClothingRepository(clothingDao: ClothingDao): ClothingRespository {
        return ClosetRepositoryImpl(clothingDao)
    }

    @Provides
    @Singleton
    fun provideDetailRepository(clothingDao: ClothingDao): DetailRepository {
        return DetailRepositoryImpl(clothingDao)
    }
}