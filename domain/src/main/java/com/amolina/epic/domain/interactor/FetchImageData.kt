package com.amolina.epic.domain.interactor

import com.amolina.epic.domain.model.ImagesData
import com.amolina.epic.domain.repository.EpicRepository
import javax.inject.Inject

class FetchImageData @Inject constructor(
  private val epicRepository: EpicRepository,
) {
  suspend fun invoke(date: String): List<ImagesData> {
    return epicRepository.getImageData(date = date)
  }
}