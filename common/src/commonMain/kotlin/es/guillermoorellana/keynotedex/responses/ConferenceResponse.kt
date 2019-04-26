package es.guillermoorellana.keynotedex.responses

import es.guillermoorellana.keynotedex.dto.Conference
import kotlinx.serialization.Serializable

@Serializable
data class ConferenceResponse(val conference: Conference)
