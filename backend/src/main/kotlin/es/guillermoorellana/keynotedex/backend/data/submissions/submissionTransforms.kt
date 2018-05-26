package es.guillermoorellana.keynotedex.backend.data.submissions

import org.jetbrains.squash.results.*
import es.guillermoorellana.keynotedex.dto.Submission as DtoSubmission

fun transformSubmission(it: ResultRow): Submission =
    Submission(
        id = it[SubmissionsTable.id],
        submitterId = it[SubmissionsTable.submitter],
        isPublic = it[SubmissionsTable.public],
        title = it[SubmissionsTable.title],
        abstract = it[SubmissionsTable.abstract]
    )

fun Submission.toDto() = DtoSubmission(
    submissionId = id,
    title = title,
    abstract = abstract,
    userId = submitterId
)
