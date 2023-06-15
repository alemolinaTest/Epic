package com.amolina.epic.data.db

import com.amolina.epic.domain.model.DatesData
import com.amolina.epic.domain.model.ImagesData
import com.amolina.epic.domain.others.InvalidListException

class EpicCache(private val epicDataBase: EpicDataBase) {
  val dao = epicDataBase.epicDao()

  suspend fun getAllDates(): List<DatesData> {
    val list = dao.getAllDates()
    if (list.isEmpty()) {
      throw InvalidListException()
    }
    return list.map {
      it.toModel()
    }
  }

  suspend fun insertDates(list: List<DatesData>) {
    clearAllDates()
    dao.saveAllDates(list.map {
      DateEntity(date = it.date)
    })
  }

  private suspend fun clearAllDates() {
    dao.deleteAllDates()
  }

  suspend fun insertImages(
    list: List<ImagesData>,
    searchDate: String,
  ) {
    clearAllImages()
    dao.saveAllImages(list.map {
      DateImageEntity(identifier = it.identifier,
                      caption = it.caption,
                      image = it.image,
                      version = it.version,
                      date = it.date,
                      searchDate = searchDate)
    })
  }

  private suspend fun clearAllImages() {
    dao.deleteAllDates()
  }

  suspend fun getImagesForDate(searchDate: String): List<ImagesData> {
    val dao = epicDataBase.epicDao()
    val list = dao.getImagesForDate(searchDate = searchDate)
    if (list.isEmpty()) {
      throw InvalidListException()
    }
    return list.map {
      it.toModel()
    }
  }
}