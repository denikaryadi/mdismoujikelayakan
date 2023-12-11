package com.komida.co.id.mdisujikelayakan.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NIKDistrict(
    val id: String,
    val name: String,
    val zipCode: String,
)