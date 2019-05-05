package es.guillermoorellana.keynotedex.datasource.responses

import es.guillermoorellana.keynotedex.datasource.dto.Submission
import kotlinx.serialization.Serializable

@Serializable
data class SubmissionResponse(val submission: Submission)
