package es.guillermoorellana.keynotedex.backend.submission

import es.guillermoorellana.keynotedex.backend.*
import es.guillermoorellana.keynotedex.backend.dao.tables.*
import es.guillermoorellana.keynotedex.responses.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.submission(dao: SubmissionStorage) {
    accept(ContentType.Application.Json) {
        get<SubmissionPage> {
            when {
                it.submissionId == null || it.submissionId.isNullOrBlank() -> {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Bad request"))
                }
                else -> {
                    val submission = dao.submissionById(it.submissionId)
                    when (submission) {
                        null -> call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
                        else -> call.respond(SubmissionResponse(submission.toDto()))
                    }
                }
            }
        }
        post<SubmissionPage> {

        }
    }
}

