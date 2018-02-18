package es.guillermoorellana.keynotedex.web.model

data class User(
    val userId: String,
    val displayName: String
)

fun es.guillermoorellana.keynotedex.dto.User.toModel() = User(
    userId = userId,
    displayName = displayName ?: userId
)
