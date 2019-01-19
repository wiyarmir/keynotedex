package es.guillermoorellana.keynotedex.web.model

import es.guillermoorellana.keynotedex.dto.Submission as DtoSubmission
import es.guillermoorellana.keynotedex.dto.SubmissionVisibility as DtoSubmissionVisibility

data class Submission(
    val submissionId: String,
    val userId: String,
    val title: String,
    val abstract: String,
    val type: String,
    val submittedTo: String,
    val visibility: SubmissionVisibility
)

enum class SubmissionVisibility {
    PUBLIC, PRIVATE
}

fun SubmissionVisibility.string() = when (this) {
    SubmissionVisibility.PUBLIC -> "public"
    SubmissionVisibility.PRIVATE -> "private"
}

fun DtoSubmission.toModel() = Submission(
    submissionId = submissionId,
    userId = userId,
    title = title,
    abstract = abstract ?: "",
    type = type ?: "",
    submittedTo = submittedTo ?: "",
    visibility = when (visibility) {
        DtoSubmissionVisibility.PRIVATE -> SubmissionVisibility.PRIVATE
        DtoSubmissionVisibility.PUBLIC -> SubmissionVisibility.PUBLIC
    }
)

fun List<DtoSubmission>.toModel(): List<Submission> = map(DtoSubmission::toModel)

fun Submission.toDto() = DtoSubmission(
    submissionId = submissionId,
    userId = userId,
    title = title,
    abstract = abstract,
    type = type,
    submittedTo = submittedTo,
    visibility = when (visibility) {
        SubmissionVisibility.PRIVATE -> DtoSubmissionVisibility.PRIVATE
        SubmissionVisibility.PUBLIC -> DtoSubmissionVisibility.PUBLIC
    }
)
