package es.guillermoorellana.keynotedex.requests

import es.guillermoorellana.keynotedex.dto.*
import kotlinx.serialization.*

@Serializable
data class UserProfileUpdateRequest(val user: User)
