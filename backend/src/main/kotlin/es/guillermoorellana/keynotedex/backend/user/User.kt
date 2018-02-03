package es.guillermoorellana.keynotedex.backend.user

import es.guillermoorellana.keynotedex.backend.submission.*

data class User(
    val userId: String,
    val email: String? = null,
    val displayName: String? = null,
    val passwordHash: String,
    val submissions: List<Submission> = emptyList()
)
