package es.guillermoorellana.keynotedex.backend.api.user

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.UserEndpoint
import es.guillermoorellana.keynotedex.backend.api.doUserProfileResponse
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.backend.data.users.toDao
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import es.guillermoorellana.keynotedex.responses.UserProfileResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import io.ktor.routing.contentType

fun Route.PutUser(userStorage: UserStorage, submissionStorage: SubmissionStorage) {

    JsonSerializableConverter.register(UserProfileUpdateRequest.serializer())
    JsonSerializableConverter.register(UserProfileResponse.serializer())

    accept(ContentType.Application.Json) {
        contentType(ContentType.Application.Json) {
            put<UserEndpoint> {
                val request: UserProfileUpdateRequest = call.receive()
                val sessionUser = getCurrentLoggedUser(userStorage)
                if (request.user.userId != sessionUser?.userId) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse("Nope"))
                }
                userStorage.updateUser(request.user.toDao())
                val updatedUser = userStorage.retrieveUser(request.user.userId)!!
                doUserProfileResponse(updatedUser, submissionStorage, sessionUser)
                call.respond(HttpStatusCode.Accepted, UserProfileResponse(updatedUser.toDto()))
            }
        }
    }
}
