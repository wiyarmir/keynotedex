package es.guillermoorellana.keynotedex.datasource.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val message: String)
