package es.guillermoorellana.keynotedex.responses

import es.guillermoorellana.keynotedex.dto.Submission
import kotlinx.serialization.Serializable

@Serializable
data class SubmissionResponse(val submission: Submission)
