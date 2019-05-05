package es.guillermoorellana.keynotedex.backend.data.submissions

import es.guillermoorellana.keynotedex.backend.data.hashids
import es.guillermoorellana.keynotedex.datasource.dto.SubmissionVisibility.PRIVATE
import es.guillermoorellana.keynotedex.datasource.dto.SubmissionVisibility.PUBLIC
import org.jetbrains.squash.results.ResultRow
import org.jetbrains.squash.results.get
import es.guillermoorellana.keynotedex.datasource.dto.Submission as DtoSubmission

fun transformSubmission(it: ResultRow): Submission =
    Submission(
        id = it[SubmissionsTable.id],
        submitterId = it[SubmissionsTable.submitter],
        isPublic = it[SubmissionsTable.public],
        title = it[SubmissionsTable.title],
        abstract = it[SubmissionsTable.abstract]
    )

fun Submission.toDto() = DtoSubmission(
    submissionId = hashids.encode(id),
    title = title,
    abstract = abstract,
    userId = submitterId,
    visibility = if (isPublic) PUBLIC else PRIVATE
)
