package com.amolina.epic.domain.interactor

import com.amolina.epic.domain.model.DatesCollection
import com.amolina.epic.domain.repository.EpicRepository
import javax.inject.Inject

class FetchDates @Inject constructor(
  private val epicRepository: EpicRepository,
) {
  suspend fun invoke(): DatesCollection {
    return epicRepository.getAllDates()
  }
}