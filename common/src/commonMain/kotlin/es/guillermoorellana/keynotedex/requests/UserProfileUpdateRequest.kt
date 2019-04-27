package es.guillermoorellana.keynotedex.requests

import es.guillermoorellana.keynotedex.dto.User
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileUpdateRequest(val user: User)
