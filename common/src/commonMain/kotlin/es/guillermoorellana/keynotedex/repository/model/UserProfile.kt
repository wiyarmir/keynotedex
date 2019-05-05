package es.guillermoorellana.keynotedex.repository.model

import es.guillermoorellana.keynotedex.datasource.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.datasource.responses.UserProfileResponse

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
