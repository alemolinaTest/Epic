package com.amolina.epic.data.di

import com.amolina.epic.data.api.ApiServices
import com.amolina.epic.data.repository.DataEpicRepository
import com.amolina.epic.domain.interactor.FetchDates
import com.amolina.epic.domain.interactor.FetchImageData
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
  fun provideApiService(retrofit: Retrofit): ApiServices =
    retrofit.create(ApiServices::class.java)

  @Provides
  @Singleton
  fun provideEpicRepository(
    apiService: ApiServices
  ): EpicRepository =
    DataEpicRepository(apiService = apiService)

}