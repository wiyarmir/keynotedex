package es.guillermoorellana.keynotedex.web.model

data class SessionPreview(
    val userId: String,
    val userDisplayName: String,
    val sessionId: String,
    val title: String
)
