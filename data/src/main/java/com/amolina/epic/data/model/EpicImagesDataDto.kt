package com.amolina.epic.data.model

import com.amolina.epic.domain.model.ImagesData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class EpicImagesDataDto(
  var identifier: String? = null,
  var caption: String? = null,
  var image: String? = null,
  var version: String? = null,
  var date: String? = null,
) {

  fun toModel(): ImagesData =
    ImagesData(
      identifier = identifier,
      caption = caption,
      image = image,
      version = version,
      date = date
    )
}