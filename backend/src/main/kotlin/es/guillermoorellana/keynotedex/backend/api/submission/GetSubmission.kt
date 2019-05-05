package es.guillermoorellana.keynotedex.backend.api.submission

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.hashids
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.data.submissions.toDto
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.datasource.responses.ErrorResponse
import es.guillermoorellana.keynotedex.datasource.responses.SubmissionResponse
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

@UseExperimental(KtorExperimentalLocationsAPI::class)
fun Route.getSubmission(submissionStorage: SubmissionStorage, userStorage: UserStorage) {

    JsonSerializableConverter.register(SubmissionResponse.serializer())

    accept(ContentType.Application.Json) {
        authenticate(optional = true) {
            get<SubmissionsEndpoint> { (submissionId) ->
                val submission = submissionId
                    ?.let { hashids.decode(it).firstOrNull() }
                    ?.let { submissionStorage.getById(it) }
                if (submission == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
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
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
                }
            }
        }
    }
}
