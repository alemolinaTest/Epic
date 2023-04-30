package com.amolina.epic.di

import com.amolina.epic.domain.interactor.FetchDates
import com.amolina.epic.domain.interactor.FetchImageData
import com.amolina.epic.domain.repository.EpicRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideFetchDatesUseCase(epicRepository: EpicRepository) =
        FetchDates(epicRepository = epicRepository)

    @Provides
    @Singleton
    fun provideFetchImageDataUseCase(epicRepository: EpicRepository) =
        FetchImageData(epicRepository = epicRepository)
}