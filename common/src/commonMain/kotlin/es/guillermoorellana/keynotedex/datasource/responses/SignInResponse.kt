package es.guillermoorellana.keynotedex.datasource.responses

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(val jwtToken: String)
