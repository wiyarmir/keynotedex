package es.guillermoorellana.keynotedex.backend.api.user

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.api.doUserProfileResponse
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import es.guillermoorellana.keynotedex.responses.UserProfileResponse
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.getUser(userStorage: UserStorage, submissionStorage: SubmissionStorage) {

    JsonSerializableConverter.register(UserProfileResponse.serializer())

    accept(ContentType.Application.Json) {
        authenticate(optional = true) {
            get<UserEndpoint> { (userId) ->
                val user = userStorage.retrieveUser(userId)
                if (user == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse("User $userId doesn't exist")
                    )
                    return@get
                }
                val currentUser = getCurrentLoggedUser(userStorage)
                doUserProfileResponse(HttpStatusCode.OK, user, submissionStorage, currentUser)
            }
        }
    }
}
