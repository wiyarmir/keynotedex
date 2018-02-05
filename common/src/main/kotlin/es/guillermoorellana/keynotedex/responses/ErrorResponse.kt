package es.guillermoorellana.keynotedex.responses

import kotlinx.serialization.*

@Serializable
data class ErrorResponse(val message: String)
