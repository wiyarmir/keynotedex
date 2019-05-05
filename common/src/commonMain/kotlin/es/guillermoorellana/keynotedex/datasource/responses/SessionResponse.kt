package es.guillermoorellana.keynotedex.datasource.responses

import es.guillermoorellana.keynotedex.datasource.dto.Session
import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(val session: Session)
