package es.guillermoorellana.keynotedex.backend.api

import es.guillermoorellana.keynotedex.backend.UserEndpoint
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.GetUser(userStorage: UserStorage, submissionStorage: SubmissionStorage) {
    accept(ContentType.Application.Json) {
        get<UserEndpoint> {
            val userId = it.userId
            val user = userStorage.retrieveUser(userId)
            if (user == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("User $userId doesn't exist")
                )
                return@get
            }
            val currentUser = getCurrentLoggedUser(userStorage)
            doUserProfileResponse(user, submissionStorage, currentUser)
        }
    }
}
