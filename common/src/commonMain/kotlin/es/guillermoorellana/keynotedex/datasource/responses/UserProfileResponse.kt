package es.guillermoorellana.keynotedex.datasource.responses

import es.guillermoorellana.keynotedex.datasource.dto.Session
import es.guillermoorellana.keynotedex.datasource.dto.User
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val user: User,
    val sessions: List<Session> = emptyList(),
    val editable: Boolean = false
)
