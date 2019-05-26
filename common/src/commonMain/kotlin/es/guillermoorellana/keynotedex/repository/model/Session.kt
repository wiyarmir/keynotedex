package es.guillermoorellana.keynotedex.repository.model

import es.guillermoorellana.keynotedex.datasource.responses.SessionResponse
import es.guillermoorellana.keynotedex.datasource.dto.Session as DtoSession
import es.guillermoorellana.keynotedex.datasource.dto.SessionVisibility as DtoSessionVisibility

data class Session(
    val sessionId: String,
    val userId: String,
    val title: String,
    val abstract: String,
    val type: String,
    val submittedTo: String,
    val visibility: SessionVisibility
)

enum class SessionVisibility {
    PUBLIC, PRIVATE
}

fun SessionVisibility.flip() = when (this) {
    SessionVisibility.PUBLIC -> SessionVisibility.PRIVATE
    SessionVisibility.PRIVATE -> SessionVisibility.PUBLIC
}

fun SessionVisibility.string() = when (this) {
    SessionVisibility.PUBLIC -> "public"
    SessionVisibility.PRIVATE -> "private"
}

fun SessionResponse.toModel() = session.toModel()

private fun DtoSession.toModel() = Session(
    sessionId = sessionId,
    userId = userId,
    title = title,
    abstract = abstract ?: "",
    type = type ?: "",
    submittedTo = submittedTo ?: "",
    visibility = when (visibility) {
        DtoSessionVisibility.PRIVATE -> SessionVisibility.PRIVATE
        DtoSessionVisibility.PUBLIC -> SessionVisibility.PUBLIC
    }
)

fun List<DtoSession>.toModel(): List<Session> = map(DtoSession::toModel)

fun Session.toDto() = DtoSession(
    sessionId = sessionId,
    userId = userId,
    title = title,
    abstract = abstract,
    type = type,
    submittedTo = submittedTo,
    visibility = when (visibility) {
        SessionVisibility.PRIVATE -> DtoSessionVisibility.PRIVATE
        SessionVisibility.PUBLIC -> DtoSessionVisibility.PUBLIC
    }
)
