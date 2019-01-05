package es.guillermoorellana.keynotedex.dto

import kotlinx.serialization.*

@Serializable
data class Submission(
    val userId: String,
    val submissionId: String,
    val title: String,
    val abstract: String? = null,
    val type: String? = null,
    val submittedTo: String? = null,
    val visibility: SubmissionVisibility
)

enum class SubmissionVisibility {
    PRIVATE, PUBLIC
}
