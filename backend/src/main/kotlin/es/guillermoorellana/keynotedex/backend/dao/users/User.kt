package es.guillermoorellana.keynotedex.backend.dao.users

import es.guillermoorellana.keynotedex.backend.dao.submissions.*

data class User(
    val userId: String,
    val email: String? = null,
    val displayName: String? = null,
    val passwordHash: String,
    val submissions: List<Submission> = emptyList()
)
