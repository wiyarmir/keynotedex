package es.guillermoorellana.keynotedex.backend.dao.users

import es.guillermoorellana.keynotedex.backend.dao.submissions.Submission
import es.guillermoorellana.keynotedex.dto.User as DtoUser

data class User(
    val userId: String,
    val email: String? = null,
    val displayName: String? = null,
    val passwordHash: String? = null,
    val submissions: List<Submission> = emptyList()
)

fun DtoUser.toDao() = User(
    userId = userId,
    displayName = displayName
)
