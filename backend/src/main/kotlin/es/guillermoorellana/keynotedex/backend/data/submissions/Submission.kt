package es.guillermoorellana.keynotedex.backend.data.submissions

import es.guillermoorellana.keynotedex.backend.data.hashids
import es.guillermoorellana.keynotedex.dto.SubmissionVisibility
import es.guillermoorellana.keynotedex.requests.SubmissionCreateRequest
import es.guillermoorellana.keynotedex.dto.Submission as DtoSubmission

data class Submission(
    val id: Long,
    val title: String,
    val abstract: String?,
    val isPublic: Boolean,
    val submitterId: String
)

fun DtoSubmission.toDao(): Submission = Submission(
    id = hashids.decode(submissionId).first(),
    submitterId = userId,
    title = title,
    abstract = abstract,
    isPublic = visibility == SubmissionVisibility.PUBLIC
)

fun SubmissionCreateRequest.toDao(userId: String): Submission =
    Submission(
        id = -1,
        submitterId = userId,
        title = title,
        abstract = abstract,
        isPublic = visibility == SubmissionVisibility.PUBLIC
    )
