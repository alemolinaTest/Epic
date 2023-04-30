package com.amolina.epic.data.repository

import com.amolina.epic.data.api.ApiService
import com.amolina.epic.domain.model.DatesCollection
import com.amolina.epic.domain.model.ImagesData
import com.amolina.epic.domain.repository.EpicRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DataEpicRepository(
  private var apiService: ApiService,
  private val dispatcher: CoroutineDispatcher
) : EpicRepository {

  override suspend fun getAllDates(): DatesCollection {
    return withContext(dispatcher) {
      try {
        val dates =
          apiService.getAvailableDates()

        dates.toModel()
      } catch (e: Exception) {
        DatesCollection(
          dates = emptyList()
        )
      }
    }
  }

  override suspend fun getImageData(date: String): ImagesData {
    return withContext(dispatcher) {
      try {
        val dates = apiService.getImagesData(date = date)

        dates.toModel()
      } catch (e: Exception) {
        ImagesData(
          identifier = null,
          caption = null,
          image = null,
          version = null,
          date = null
        )
      }
    }
  }
}