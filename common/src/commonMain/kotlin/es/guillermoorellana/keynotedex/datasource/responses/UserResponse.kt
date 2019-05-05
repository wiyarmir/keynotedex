package es.guillermoorellana.keynotedex.datasource.responses

import es.guillermoorellana.keynotedex.datasource.dto.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val user: User)
