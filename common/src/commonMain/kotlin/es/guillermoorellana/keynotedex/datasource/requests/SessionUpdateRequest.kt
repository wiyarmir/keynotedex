package es.guillermoorellana.keynotedex.datasource.requests

import es.guillermoorellana.keynotedex.datasource.dto.Session
import kotlinx.serialization.Serializable

@Serializable
data class SessionUpdateRequest(val session: Session)
