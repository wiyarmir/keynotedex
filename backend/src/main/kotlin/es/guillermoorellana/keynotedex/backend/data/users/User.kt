package es.guillermoorellana.keynotedex.backend.data.users

import es.guillermoorellana.keynotedex.dto.User as DtoUser

data class User(
    val userId: String,
    val email: String? = null,
    val displayName: String? = null,
    val passwordHash: String? = null,
    val bio: String? = null
)

fun DtoUser.toDao() = User(
    userId = userId,
    displayName = displayName,
    bio = bio
)
