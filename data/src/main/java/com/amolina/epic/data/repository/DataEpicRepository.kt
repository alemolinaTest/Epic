package com.amolina.epic.data.repository

import com.amolina.epic.data.api.ApiServices
import com.amolina.epic.data.db.EpicCache
import com.amolina.epic.domain.model.DatesData
import com.amolina.epic.domain.model.ImagesData
import com.amolina.epic.domain.others.InvalidListException
import com.amolina.epic.domain.repository.EpicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataEpicRepository @Inject constructor(
  private var apiService: ApiServices,
  private val cache: EpicCache,
): EpicRepository {
  override suspend fun getAllDates(): List<DatesData>? {
    return withContext(Dispatchers.IO) {
      try { //fetch from database
        val cachedDates = cache.getAllDates()
        cachedDates
      } catch (e: InvalidListException) {
        try { //if not fetch from api endpoint
          val dates = apiService.getAvailableDates()
          val list = dates.map { it.toModel() } //save on cache
          cache.insertDates(list)
          list
        } catch (e: Exception) {
          null
        }
      }
    }
  }

  override suspend fun getImageData(searchDate: String): List<ImagesData>? {
    return withContext(Dispatchers.IO) {
      try { //fetch from database
        val cachedDates = cache.getImagesForDate(searchDate = searchDate)
        cachedDates
      } catch (e: InvalidListException) {
        try { //if not fetch from api endpoint
          val imagesData = apiService.getImagesData(date = searchDate)
          val list = imagesData.map { it.toModel() } //save on cache
          cache.insertImages(list = list,
                             searchDate = searchDate)
          list
        } catch (e: Exception) {
          null
        }
      }
    }
  }
}