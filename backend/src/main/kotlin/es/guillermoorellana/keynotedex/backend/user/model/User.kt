package es.guillermoorellana.keynotedex.backend.user.model

data class User(
    val userId: String,
    val email: String? = null,
    val displayName: String? = null,
    val passwordHash: String
)
