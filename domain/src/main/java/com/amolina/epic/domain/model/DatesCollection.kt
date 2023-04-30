package com.amolina.epic.domain.model

data class DatesCollection(private val dates: List<String>) {

  fun getAllDates(): List<String> = dates

  fun throwIfEmpty(exception: Exception) {
    if (dates.isEmpty()) throw exception
  }
}
