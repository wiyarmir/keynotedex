package es.guillermoorellana.keynotedex.backend.model

data class PublicUser(
        val userId: String,
        val displayName: String
)

fun User.toPublic() = PublicUser(
        userId = userId,
        displayName = displayName
)
