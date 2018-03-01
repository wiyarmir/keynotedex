package es.guillermoorellana.keynotedex.responses

import es.guillermoorellana.keynotedex.dto.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val user: User)
