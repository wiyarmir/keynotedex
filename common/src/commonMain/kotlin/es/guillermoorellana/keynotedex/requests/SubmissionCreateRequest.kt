package es.guillermoorellana.keynotedex.requests

import es.guillermoorellana.keynotedex.dto.SubmissionVisibility
import kotlinx.serialization.Serializable

@Serializable
data class SubmissionCreateRequest(
    val title: String,
    val abstract: String,
    val type: String? = null,
    val submittedTo: String? = null,
    val visibility: SubmissionVisibility
)
