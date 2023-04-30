package com.amolina.epic.domain.repository

import com.amolina.epic.domain.model.DatesCollection
import com.amolina.epic.domain.model.ImagesData

interface EpicRepository {

  /**
   * Fetch for all available dates
   */
  suspend fun getAllDates(): DatesCollection

  /**
   * Fetch for image data for a date
   */
  suspend fun getImageData(date: String): ImagesData
}