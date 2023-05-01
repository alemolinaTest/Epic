package com.amolina.epic.data.repository

import com.amolina.epic.data.api.ApiServices
import com.amolina.epic.domain.model.DatesData
import com.amolina.epic.domain.model.ImagesData
import com.amolina.epic.domain.repository.EpicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataEpicRepository @Inject constructor(
  private var apiService: ApiServices,
) : EpicRepository {

  override suspend fun getAllDates(): List<DatesData> {
    return withContext(Dispatchers.IO) {
      try {
        val dates =
          apiService.getAvailableDates()
        val list = mutableListOf<DatesData>()
        for (date in dates) {
          list.add(date.toModel())
        }

        list
      } catch (e: Exception) {
        listOf<DatesData>()
      }
    }
  }

  override suspend fun getImageData(date: String): List<ImagesData> {
    return withContext(Dispatchers.IO) {
      try {
        val imagesData = apiService.getImagesData(date = date)

        val list = mutableListOf<ImagesData>()
        for (imageData in imagesData) {
          val imageData = imageData.toModel(searchDate = date)
          val url = imageData.getImageDownloadIdentifier()
          list.add(imageData)
        }
        list
      } catch (e: Exception) {
        listOf<ImagesData>()
      }
    }
  }
}