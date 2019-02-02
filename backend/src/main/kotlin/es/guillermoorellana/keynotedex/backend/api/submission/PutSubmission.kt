package es.guillermoorellana.keynotedex.backend.api.submission

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.data.submissions.toDao
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.requests.SubmissionUpdateRequest
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import java.sql.SQLException

fun Route.PutSubmission(submissionStorage: SubmissionStorage, userStorage: UserStorage) {

    JsonSerializableConverter.register(SubmissionUpdateRequest.serializer())

    accept(ContentType.Application.Json) {
        put<SubmissionsEndpoint> {
            val user = getCurrentLoggedUser(userStorage)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@put
            }
            val submission = call.receive<SubmissionUpdateRequest>().submission
            if (submission.userId != user.userId) {
                call.respond(HttpStatusCode.Unauthorized)
                return@put
            }
            try {
                submissionStorage.update(submission.toDao())
            } catch (e: SQLException) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = "Failed to create submission: ${e.message}")
                )
            }
            call.respond(HttpStatusCode.Created)
        }
    }
}
