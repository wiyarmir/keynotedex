package es.guillermoorellana.keynotedex.backend.data.submissions

import es.guillermoorellana.keynotedex.dto.SubmissionVisibility
import es.guillermoorellana.keynotedex.dto.Submission as DtoSubmission

data class Submission(
    val id: String,
    val title: String,
    val abstract: String?,
    val isPublic: Boolean,
    val submitterId: String
)

fun DtoSubmission.toDao(): Submission = Submission(
    id = submissionId,
    submitterId = userId,
    title = title,
    abstract = abstract,
    isPublic = visibility == SubmissionVisibility.PUBLIC
)
