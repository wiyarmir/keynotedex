package es.guillermoorellana.keynotedex.backend.data.sessions

import es.guillermoorellana.keynotedex.backend.data.hashids
import es.guillermoorellana.keynotedex.datasource.dto.SessionVisibility
import es.guillermoorellana.keynotedex.datasource.requests.SessionCreateRequest
import es.guillermoorellana.keynotedex.datasource.dto.Session as DtoSubmission

data class Session(
    val id: Long,
    val title: String,
    val abstract: String?,
    val isPublic: Boolean,
    val submitterId: String
)

fun DtoSubmission.toDao(): Session =
    Session(
        id = hashids.decode(sessionId).first(),
        submitterId = userId,
        title = title,
        abstract = abstract,
        isPublic = visibility == SessionVisibility.PUBLIC
    )

fun SessionCreateRequest.toDao(userId: String): Session =
    Session(
        id = -1,
        submitterId = userId,
        title = title,
        abstract = abstract,
        isPublic = visibility == SessionVisibility.PUBLIC
    )
