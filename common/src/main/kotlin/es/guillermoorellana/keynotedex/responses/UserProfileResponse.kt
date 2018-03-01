package es.guillermoorellana.keynotedex.responses

import es.guillermoorellana.keynotedex.dto.Submission
import es.guillermoorellana.keynotedex.dto.User
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val user: User,
    val submissions: List<Submission> = emptyList(),
    val editable: Boolean = false
)
