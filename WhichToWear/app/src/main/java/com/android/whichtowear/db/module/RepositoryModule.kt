package com.android.whichtowear.db.module

import android.content.Context
import com.android.whichtowear.db.Impl.ClosetRepositoryImpl
import com.android.whichtowear.db.Impl.DetailRepositoryImpl
import com.android.whichtowear.db.dao.ClothingDao
import com.android.whichtowear.db.repository.ClothingRespository
import com.android.whichtowear.db.repository.DetailRepository
import com.android.whichtowear.survey.PhotoUriManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideDetailRepository(clothingDao: ClothingDao): DetailRepository {
        return DetailRepositoryImpl(clothingDao)
    }

    @Provides
    @Singleton
    fun providePhotoUriManager(
        @ApplicationContext appContext: Context
    ): PhotoUriManager{
        return PhotoUriManager(appContext)
    }
}