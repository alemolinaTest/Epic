package com.amolina.epic.data.repository

import com.amolina.epic.data.api.ApiServices
import com.amolina.epic.domain.model.DatesCollection
import com.amolina.epic.domain.model.ImagesData
import com.amolina.epic.domain.repository.EpicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataEpicRepository @Inject constructor(
  private var apiService: ApiServices,
) : EpicRepository {

  override suspend fun getAllDates(): DatesCollection {
    return withContext(Dispatchers.IO) {
      try {
        val dates =
          apiService.getAvailableDates()
        val list = mutableListOf<String>()
        for (date in dates) {
          list.add(date.date)
        }

        DatesCollection(
          dates = list
        )
      } catch (e: Exception) {
        DatesCollection(
          dates = emptyList()
        )
      }
    }
  }

  override suspend fun getImageData(date: String): List<String> {
    return withContext(Dispatchers.IO) {
      try {
        val imagesData = apiService.getImagesData(date = date)

        val list = mutableListOf<String>()
        for (imageData in imagesData) {
          val model= imageData.toModel()
          val url= model.getImageDownloadIdentifier()
          list.add(url)
        }
        list
      } catch (e: Exception) {
        listOf(
          ""
        )
      }
    }
  }
}