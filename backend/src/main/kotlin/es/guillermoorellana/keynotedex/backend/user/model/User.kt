package es.guillermoorellana.keynotedex.backend.user.model

data class User(
        val userId: String,
        val email: String,
        val displayName: String,
        val passwordHash: String
)
