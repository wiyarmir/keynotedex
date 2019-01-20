package es.guillermoorellana.keynotedex.web.model

import es.guillermoorellana.keynotedex.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.responses.UserProfileResponse

data class UserProfile(
    val user: User,
    val sessions: List<Session>,
    val editable: Boolean
)

fun UserProfileResponse.toModel() = UserProfile(
    user = user.toModel(),
    sessions = submissions.toModel(),
    editable = editable
)

fun UserProfile.toUpdateRequest() = UserProfileUpdateRequest(
    user = user.toDto()
)
