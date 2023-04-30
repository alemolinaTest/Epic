package com.amolina.epic.data.model

import com.amolina.epic.domain.model.DatesCollection
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class EpicDatesDto(var date: List<String>? = null) {

  fun toModel(): DatesCollection = if (!date.isNullOrEmpty()) {
    DatesCollection(dates = date!!.map { it })
  } else {
    DatesCollection(
      listOf()
    )
  }
}