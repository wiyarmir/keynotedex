package es.guillermoorellana.keynotedex.backend.external.json

import kotlinx.serialization.Serializable
import es.guillermoorellana.keynotedex.backend.data.conferences.Conference as DaoConference

@Serializable
data class Conference(
    val name: String,
    val url: String,
    val startDate: String,
    val endDate: String? = null,
    val city: String,
    val country: String,
    val twitter: String? = null,
    val cfpStartDate: String? = null,
    val cfpEndDate: String? = null,
    val cfpUrl: String? = null
)

fun Conference.toDao() = DaoConference(
    name = name,
    dateStart = startDate,
    dateEnd = endDate ?: startDate,
    location = listOf(city, country).joinToString(),
    website = url,
    twitter = twitter,
    cfpStart = cfpStartDate,
    cfpEnd = cfpEndDate,
    cfpSite = cfpUrl
)
