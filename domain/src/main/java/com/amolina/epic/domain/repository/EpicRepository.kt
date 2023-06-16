package com.amolina.epic.domain.repository

import com.amolina.epic.domain.model.DatesData
import com.amolina.epic.domain.model.ImagesData

interface EpicRepository {
  /**
   * Fetch for all available dates
   */
  suspend fun getAllDates(): List<DatesData>?

  /**
   * Fetch for all available dates
   */
  suspend fun getAllRefreshedDates(): List<DatesData>?

  /**
   * Fetch for image data for a date
   */
  suspend fun getImagesData(searchDate: String): List<ImagesData>?
}