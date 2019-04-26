package es.guillermoorellana.keynotedex.responses

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(val jwtToken: String)
