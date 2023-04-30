package com.amolina.epic.domain.model

data class ImagesData(
  private var identifier: String? = null,
  private var caption: String? = null,
  private var image: String? = null,
  private var version: String? = null,
  private var date: String? = null,
) {

  fun getIdentifier(): String = identifier.toString()

}
