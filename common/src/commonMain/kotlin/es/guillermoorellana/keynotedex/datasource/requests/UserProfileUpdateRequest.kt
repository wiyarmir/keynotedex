package es.guillermoorellana.keynotedex.datasource.requests

import es.guillermoorellana.keynotedex.datasource.dto.User
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileUpdateRequest(val user: User)
