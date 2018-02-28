package es.guillermoorellana.keynotedex.web.model

import es.guillermoorellana.keynotedex.requests.*
import es.guillermoorellana.keynotedex.responses.*

data class UserProfile(
    val user: User,
    val submissions: List<Submission>,
    val editable: Boolean
)

fun UserProfileResponse.toModel() = UserProfile(
    user = user.toModel(),
    submissions = submissions.toModel(),
    editable = editable
)

fun UserProfile.toUpdateRequest() = UserProfileUpdateRequest(
    user = user.toDto()
)
