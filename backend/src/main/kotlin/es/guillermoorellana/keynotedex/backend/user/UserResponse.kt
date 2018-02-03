package es.guillermoorellana.keynotedex.backend.user

import es.guillermoorellana.keynotedex.backend.submission.*

data class UserResponse(val user: PublicUser)
data class PublicUser(
    val userId: String,
    val displayName: String?,
    val submissions: List<Submission> = emptyList()
)

fun User.toPublic() =
    PublicUser(
        userId = userId,
        displayName = displayName,
        submissions = submissions
    )
