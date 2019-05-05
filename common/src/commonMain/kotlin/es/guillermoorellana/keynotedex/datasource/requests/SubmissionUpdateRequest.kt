package es.guillermoorellana.keynotedex.datasource.requests

import es.guillermoorellana.keynotedex.datasource.dto.Submission
import kotlinx.serialization.Serializable

@Serializable
data class SubmissionUpdateRequest(val submission: Submission)
