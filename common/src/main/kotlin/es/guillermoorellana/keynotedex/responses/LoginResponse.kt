package es.guillermoorellana.keynotedex.responses

import es.guillermoorellana.keynotedex.dto.*
import kotlinx.serialization.*

@Serializable
data class LoginResponse(val user: User)
