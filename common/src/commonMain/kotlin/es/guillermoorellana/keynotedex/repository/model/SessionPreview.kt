package es.guillermoorellana.keynotedex.repository.model

data class SessionPreview(
    val userId: String,
    val userDisplayName: String,
    val sessionId: String,
    val title: String
)
