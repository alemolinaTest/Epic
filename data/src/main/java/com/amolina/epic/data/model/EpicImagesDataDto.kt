package com.amolina.epic.data.model

import com.amolina.epic.domain.model.ImagesData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class EpicImagesDataDto(
  var identifier: String? = "",
  var caption: String? = null,
  var image: String? = "",
  var version: String? = null,
  var date: String? = null,
) {

  fun toModel(searchDate: String): ImagesData {
    return ImagesData(
      identifier = identifier.toString(),
      caption = caption,
      image = image.toString(),
      version = version,
      date = date,
      searchDate = searchDate
    )
  }
}