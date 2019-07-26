package es.guillermoorellana.keynotedex.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class Conference(
    val name: String,
    val dateStart: String,
    val dateEnd: String,
    val location: String?,
    val website: String?,
    val twitter: String?,
    val cfpStart: String?,
    val cfpEnd: String?,
    val cfpSite: String?
)
