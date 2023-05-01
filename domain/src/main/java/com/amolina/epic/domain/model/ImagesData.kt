package com.amolina.epic.domain.model

data class ImagesData(
  private var identifier: String? = null,
  private var caption: String? = null,
  private var image: String? = null,
  private var version: String? = null,
  private var date: String? = null,
  private var downloaded: Boolean = false
) {

  companion object {
    const val BASE_FILE_DOWNLOAD = "https://epic.gsfc.nasa.gov/archive/enhanced/"
  }

  fun getIdentifier(): String = identifier.toString()

  //2018/06/01/png/epic_RGB_20180601105347.png
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

  fun isDownloaded(): Boolean = downloaded
}
