package es.guillermoorellana.keynotedex.backend.dao.submissions

import org.jetbrains.squash.results.*

fun transformSubmission(it: ResultRow): Submission =
    Submission(
        id = it[Submissions.id],
        submitterId = it[Submissions.submitter],
        isPublic = it[Submissions.public],
        title = it[Submissions.title],
        abstract = it[Submissions.abstract]
    )

fun Submission.toDto(): es.guillermoorellana.keynotedex.dto.Submission =
    es.guillermoorellana.keynotedex.dto.Submission(
        submissionId = id,
        title = title,
        abstract = abstract
    )
