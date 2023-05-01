package com.amolina.epic.data.api

import com.amolina.epic.data.model.EpicDatesDto
import com.amolina.epic.data.model.EpicImagesDataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

  companion object {
    private const val EPIC_DATES = "enhanced/all/"
    private const val EPIC_DATE = "enhanced/date/"
    private const val EPIC_FILE= "epic.gsfc.nasa.gov/archive/enhanced/"
  }

  @GET( EPIC_DATES)
  suspend fun getAvailableDates(): List<EpicDatesDto>

  @GET( EPIC_DATE)
  suspend fun getImagesData( @Query("date") date: String): List<EpicImagesDataDto>
}