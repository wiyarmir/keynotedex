package es.guillermoorellana.keynotedex.backend.dao.tables

import org.jetbrains.squash.definition.*
import org.jetbrains.squash.results.*

object Submissions : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val submitter = varchar("submitter", 20).index()
    val title = varchar("title", 200)
    val abstract = varchar("abstract", 2000).nullable()
}

data class Submission(val title: String, val abstract: String?)

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
