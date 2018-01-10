package es.guillermoorellana.conferences.backend.model

data class User(
        val userId: String,
        val email: String,
        val displayName: String,
        val passwordHash: String
)
