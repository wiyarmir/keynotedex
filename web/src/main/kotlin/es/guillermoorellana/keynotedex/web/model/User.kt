package es.guillermoorellana.keynotedex.web.model

import es.guillermoorellana.keynotedex.dto.User as DtoUser

data class User(
    val userId: String,
    val displayName: String
)

fun DtoUser.toModel() = User(
    userId = userId,
    displayName = displayName ?: userId
)

fun User.toDto() = DtoUser(
    userId = userId,
    displayName = displayName
)
