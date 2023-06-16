package com.amolina.epic.domain.interactor

import com.amolina.epic.domain.model.DatesData
import com.amolina.epic.domain.repository.EpicRepository
import javax.inject.Inject

class FetchDates @Inject constructor(
  private val epicRepository: EpicRepository,
) {
  suspend operator fun invoke(): List<DatesData>? = epicRepository.getAllDates()

  suspend fun refreshData(): List<DatesData>? = epicRepository.getAllRefreshedDates()
}