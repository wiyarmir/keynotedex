package es.guillermoorellana.keynotedex.backend.dao.submissions

import org.jetbrains.squash.results.*

fun transformSubmission(it: ResultRow): Submission =
    Submission(
        title = it[Submissions.title],
        abstract = it[Submissions.abstract]
    )

fun Submission.toDto(): es.guillermoorellana.keynotedex.dto.Submission =
    es.guillermoorellana.keynotedex.dto.Submission(
        title = title,
        abstract = abstract
    )
