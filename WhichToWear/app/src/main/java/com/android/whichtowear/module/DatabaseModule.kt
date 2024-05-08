package com.android.whichtowear.module

import android.app.Application
import androidx.room.Room
import com.android.whichtowear.dao.ClothingDao
import com.android.whichtowear.database.ClothingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDataBase(application: Application): ClothingDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            ClothingDatabase::class.java,
            "Chose"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideClothingDao(clothingDatabase: ClothingDatabase): ClothingDao {
        return clothingDatabase.clothingDao()
    }

}