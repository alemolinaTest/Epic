package com.amolina.epic.data.model

import com.amolina.epic.domain.model.DatesCollection
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class EpicDatesDto(var date: String) {

  fun toModel(): DatesCollection = DatesCollection(listOf())

//  fun toModel(): DatesCollection = if (!dates.isNullOrEmpty()) {
//    DatesCollection(dates = dates!!.map { it.date!! })
//  } else {
//    DatesCollection(
//      listOf()
//    )
//  }
}