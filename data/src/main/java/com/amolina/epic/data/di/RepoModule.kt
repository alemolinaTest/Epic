package com.amolina.epic.data.di

import com.amolina.epic.data.api.ApiService
import com.amolina.epic.data.repository.DataEpicRepository
import com.amolina.epic.domain.repository.EpicRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

  @Provides
  fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

  @Provides
  @Singleton
  fun provideEpicRepository(
    apiService: ApiService,
    @IODispatcher dispatcher: CoroutineDispatcher
  ): EpicRepository =
    DataEpicRepository(apiService = apiService, dispatcher = dispatcher)

}