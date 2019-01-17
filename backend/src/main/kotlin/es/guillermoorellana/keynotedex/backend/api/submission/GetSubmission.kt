package es.guillermoorellana.keynotedex.backend.api.submission

import es.guillermoorellana.keynotedex.backend.SubmissionsEndpoint
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.data.submissions.toDto
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import es.guillermoorellana.keynotedex.responses.SubmissionResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.GetSubmission(submissionStorage: SubmissionStorage, userStorage: UserStorage) {
    accept(ContentType.Application.Json) {
        get<SubmissionsEndpoint> { (submissionId) ->
            val submission = submissionStorage.submissionById(submissionId)
            if (submission == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Not found")
                )
                return@get
            }
            if (submission.isPublic) {
                call.respond(SubmissionResponse(submission.toDto()))
                return@get
            }
            val user = getCurrentLoggedUser(userStorage)
            if (user?.userId == submission.submitterId) {
                call.respond(SubmissionResponse(submission.toDto()))
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Not found")
                )
            }
        }
    }
}
