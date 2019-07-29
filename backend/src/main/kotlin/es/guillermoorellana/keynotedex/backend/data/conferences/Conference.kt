package es.guillermoorellana.keynotedex.backend.data.conferences

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
