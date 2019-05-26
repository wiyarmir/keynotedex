package es.guillermoorellana.keynotedex.backend.data.sessions

import es.guillermoorellana.keynotedex.backend.data.hashids
import es.guillermoorellana.keynotedex.datasource.dto.SessionVisibility.PRIVATE
import es.guillermoorellana.keynotedex.datasource.dto.SessionVisibility.PUBLIC
import org.jetbrains.squash.results.ResultRow
import org.jetbrains.squash.results.get
import es.guillermoorellana.keynotedex.datasource.dto.Session as DtoSubmission

fun transformSession(it: ResultRow): Session =
    Session(
        id = it[SessionsTable.id],
        submitterId = it[SessionsTable.submitter],
        isPublic = it[SessionsTable.public],
        title = it[SessionsTable.title],
        abstract = it[SessionsTable.abstract]
    )

fun Session.toDto() = DtoSubmission(
    sessionId = hashids.encode(id),
    title = title,
    abstract = abstract,
    userId = submitterId,
    visibility = if (isPublic) PUBLIC else PRIVATE
)
