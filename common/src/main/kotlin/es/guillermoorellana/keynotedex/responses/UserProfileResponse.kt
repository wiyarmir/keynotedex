package es.guillermoorellana.keynotedex.responses

import es.guillermoorellana.keynotedex.dto.*
import kotlinx.serialization.*

@Serializable
data class UserProfileResponse(
    val user: User,
    val submissions: List<Submission> = emptyList(),
    val editable: Boolean = false
)
