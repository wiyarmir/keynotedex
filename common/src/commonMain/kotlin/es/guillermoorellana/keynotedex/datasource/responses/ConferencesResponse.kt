package es.guillermoorellana.keynotedex.datasource.responses

import es.guillermoorellana.keynotedex.datasource.dto.Conference
import kotlinx.serialization.Serializable

@Serializable
data class ConferencesResponse(val conferences: List<Conference>)
