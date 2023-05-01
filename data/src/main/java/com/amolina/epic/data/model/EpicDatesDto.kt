package com.amolina.epic.data.model

import com.amolina.epic.domain.model.DatesData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class EpicDatesDto(var date: String) {
  fun toModel() = DatesData(date = date, downloaded = false)
}