package es.guillermoorellana.keynotedex.web.model

import es.guillermoorellana.keynotedex.dto.Submission as DtoSession
import es.guillermoorellana.keynotedex.dto.SubmissionVisibility as DtoSessionVisibility

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

fun DtoSession.toModel() = Session(
    sessionId = submissionId,
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
    submissionId = sessionId,
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
