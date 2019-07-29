package es.guillermoorellana.keynotedex.backend.external

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import es.guillermoorellana.keynotedex.backend.data.conferences.Conference as DaoConference

@Serializable
data class Conference(
    val name: String,
    @SerialName("date_start") val dateStart: String,
    @SerialName("date_end") val dateEnd: String,
    val location: String? = null,
    val website: String? = null,
    val twitter: String? = null,
    @SerialName("cfp_start") val cfpStart: String? = null,
    @SerialName("cfp_end") val cfpEnd: String? = null,
    @SerialName("cfp_site") val cfpSite: String? = null
)

fun Conference.toDao() = DaoConference(
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
