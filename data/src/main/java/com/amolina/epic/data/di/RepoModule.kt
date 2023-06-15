package com.amolina.epic.data.di

import android.app.Application
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.amolina.epic.data.api.ApiServices
import com.amolina.epic.data.db.EpicCache
import com.amolina.epic.data.db.EpicDataBase
import com.amolina.epic.data.repository.DataEpicRepository
import com.amolina.epic.domain.repository.EpicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val EPIC_DB_NAME = "epic-base"

@Module @InstallIn(SingletonComponent::class) object RepoModule {

  @Provides
  @Singleton
  fun provideEpicCache(
    epicDatabase: EpicDataBase,
  ) = EpicCache(epicDataBase = epicDatabase)

  @Provides
  @Singleton
  fun provideEpicRepository(
    apiService: ApiServices,
    cache: EpicCache,
  ): EpicRepository = DataEpicRepository(apiService = apiService,
                                         cache = cache)

  @Provides
  @Singleton
  fun createEpicDatabase(
    app: Application,
  ): EpicDataBase = Room
    .databaseBuilder(app,
                     EpicDataBase::class.java,
                     EPIC_DB_NAME)
    .fallbackToDestructiveMigrationOnDowngrade()
    .build()
}