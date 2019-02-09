package es.guillermoorellana.keynotedex.responses

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val jwtToken: String)
