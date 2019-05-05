package es.guillermoorellana.keynotedex.datasource.responses

import es.guillermoorellana.keynotedex.datasource.dto.Submission
import es.guillermoorellana.keynotedex.datasource.dto.User
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val user: User,
    val submissions: List<Submission> = emptyList(),
    val editable: Boolean = false
)
