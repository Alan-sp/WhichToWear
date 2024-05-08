package com.android.whichtowear.module

import com.android.whichtowear.Impl.ClosetRepositoryImpl
import com.android.whichtowear.dao.ClothingDao
import com.android.whichtowear.repository.ClothingRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideClothingRepository(clothingDao: ClothingDao): ClothingRespository {
        return ClosetRepositoryImpl(clothingDao)
    }
}