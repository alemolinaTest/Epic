package com.amolina.epic.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DatesDto(var date: String)