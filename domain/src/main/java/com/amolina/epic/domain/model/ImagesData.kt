package com.amolina.epic.domain.model

data class ImagesData(
  var identifier: String,
  var caption: String? = null,
  var image: String? = null,
  var version: String? = null,
  var date: String? = null,
  var url: String = "",
  var downloaded: Boolean = false
) {

  companion object {
    const val BASE_FILE_DOWNLOAD = "https://epic.gsfc.nasa.gov/archive/enhanced/"
  }

  fun getImageDownloadIdentifier(): String {
    return if (!date.isNullOrBlank()) {
      val basePart = BASE_FILE_DOWNLOAD
      val datePart = date?.substring(0, 4)
        .plus("/")
        .plus(date?.substring(5, 7))
        .plus("/")
        .plus(date?.substring(8, 10))

      val lastPart = "/png/".plus(image).plus(".png")

      basePart.plus(datePart).plus(lastPart)
    } else {
      ""
    }
  }
}
