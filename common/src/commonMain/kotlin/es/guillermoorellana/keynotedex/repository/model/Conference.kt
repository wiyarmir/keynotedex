package es.guillermoorellana.keynotedex.repository.model

import es.guillermoorellana.keynotedex.datasource.dto.Conference as DtoConference

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

fun DtoConference.toModel(): Conference = Conference(
    name = name,
    dateStart = dateStart,
    dateEnd = dateEnd,
    location = location,
    website = website,
    twitter = twitter,
    cfpStart = cfpStart,
    cfpEnd = cfpEnd,
    cfpSite = cfpSite
)
