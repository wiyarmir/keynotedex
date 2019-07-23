package es.guillermoorellana.keynotedex.backend.frontmatter

import kotlinx.serialization.Serializable

@Serializable
data class FMConference(
    val name: String,
    val location: String,
    val date_start: String,
    val date_end: String,
    val website: String? = null,
    val twitter: String? = null,
    val cfp_start: String? = null,
    val cfp_end: String? = null,
    val cfp_site: String? = null
)
