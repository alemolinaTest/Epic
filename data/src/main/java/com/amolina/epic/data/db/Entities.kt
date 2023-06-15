package com.amolina.epic.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amolina.epic.domain.model.DatesData
import com.amolina.epic.domain.model.ImagesData

@Entity(tableName = DateEntity.TABLE_NAME) data class DateEntity(
  @PrimaryKey(autoGenerate = true) val dateId: Int? = null,
  val date: String,
) {
  companion object {
    const val TABLE_NAME = "date_entity"
  }

  fun toModel(): DatesData {
    return DatesData(date = date,
                     downloaded = true)
  }
}

@Entity(tableName = DateImageEntity.TABLE_NAME) data class DateImageEntity(
  @PrimaryKey(autoGenerate = true) val dateImageId: Int? = null,
  var identifier: String,
  var caption: String?,
  var image: String?,
  var version: String?,
  var date: String?,
  var searchDate: String,
) {
  companion object {
    const val TABLE_NAME = "date_image_entity"
  }

  fun toModel(): ImagesData {
    return ImagesData(identifier = identifier,
                      caption = caption,
                      image = image,
                      version = version,
                      date = date,
                      url = "",
                      downloaded = true)
  }
}
