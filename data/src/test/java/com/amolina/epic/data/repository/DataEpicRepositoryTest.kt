package com.amolina.epic.data.repository

import com.amolina.epic.data.api.ApiServices
import com.amolina.epic.data.db.EpicCache
import com.amolina.epic.data.model.EpicDatesDto
import com.amolina.epic.domain.model.DatesData
import com.amolina.epic.domain.others.InvalidListException
import com.amolina.epic.data.testing.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi class DataEpicRepositoryTest {
  @get:Rule
  val mainCoroutineRule = MainCoroutineRule()

  @Test
  fun `when get getAllDates is called for first time, data from api endpoint, `() = mainCoroutineRule.runTest {
    val epicCache = mock<EpicCache>{
      onBlocking { getAllDates() } doThrow  InvalidListException()
    }
    val expectedDate = "2021-11-17"
    val expectedDto = listOf(EpicDatesDto(date = expectedDate))
    val epicApi = mock<ApiServices> {
      onBlocking { getAvailableDates() } doReturn expectedDto
    }
    val epicRepository = createRepository(apiServices = epicApi,
                                          epicCache = epicCache)

    val dates = epicRepository.getAllDates()

    Assert.assertEquals(dates, expectedDto)
  }

  @Test
  fun `when get getAllDates is called second time, dta should be on Cache (ROOM), `() = mainCoroutineRule.runTest {
    val expectedDate = "2021-11-17"
    val expectedData = listOf(DatesData(date = expectedDate))
    val epicCache = mock<EpicCache>{
      onBlocking { getAllDates() } doReturn expectedData
    }

    val expectedDto = listOf(EpicDatesDto(date = expectedDate))
    val epicApi = mock<ApiServices> {
      onBlocking { getAvailableDates() } doReturn expectedDto
    }
    val epicRepository = createRepository(apiServices = epicApi,
                                          epicCache = epicCache)

    val dates = epicRepository.getAllDates()

    Assert.assertEquals(dates, expectedData)
  }

  private fun createRepository(
    apiServices: ApiServices = mock(),
    epicCache: EpicCache = mock(),
  ) = DataEpicRepository(apiService = apiServices,
                         cache = epicCache)
}