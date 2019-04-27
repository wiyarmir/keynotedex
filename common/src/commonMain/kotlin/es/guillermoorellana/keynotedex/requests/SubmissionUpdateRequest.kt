package es.guillermoorellana.keynotedex.requests

import es.guillermoorellana.keynotedex.dto.Submission
import kotlinx.serialization.Serializable

@Serializable
data class SubmissionUpdateRequest(val submission: Submission)
