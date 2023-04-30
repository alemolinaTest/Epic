package com.amolina.epic.domain.model

data class ImagesData(
  private var identifier: String? = null,
  private var caption: String? = null,
  private var image: String? = null,
  private var version: String? = null,
  private var date: String? = null,
  private var downloaded: Boolean = false
) {

  fun getIdentifier(): String = identifier.toString()

  //2018/06/01/png/epic_RGB_20180601105347.png
  fun getImageDownloadIdentifier(): String? {
    return if (date.isNullOrBlank()) {
      val datePart = date?.substring(0, 4)
        .plus("/")
        .plus(date?.substring(5, 6))
        .plus("/")
        .plus(date?.substring(8, 9))

      val lastPart = "png/".plus(image).plus(".png")

      datePart.plus(lastPart)
    } else {
      null
    }
  }

  fun isDownloaded(): Boolean = downloaded
}
