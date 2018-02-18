package es.guillermoorellana.keynotedex.web.model

import es.guillermoorellana.keynotedex.responses.*

data class UserProfile(val user: User, val submissions: List<Submission>)

fun UserProfileResponse.toModel() = UserProfile(
    user = user.toModel(),
    submissions = submissions.toModel()
)
