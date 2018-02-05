package es.guillermoorellana.keynotedex.responses

import es.guillermoorellana.keynotedex.dto.*
import kotlinx.serialization.*

@Serializable
data class UserResponse(val user: User)
