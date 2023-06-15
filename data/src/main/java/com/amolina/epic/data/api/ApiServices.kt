package com.amolina.epic.data.api

import com.amolina.epic.data.model.EpicDatesDto
import com.amolina.epic.data.model.EpicImagesDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
  companion object {
    private const val EPIC_DATES = "enhanced/all/"
    private const val EPIC_IMAGES = "enhanced/date/"
  }

  @GET(EPIC_DATES)
  suspend fun getAvailableDates(): List<EpicDatesDto>

  @GET(EPIC_IMAGES)
  suspend fun getImagesData(@Query("date") date: String): List<EpicImagesDataDto>
}