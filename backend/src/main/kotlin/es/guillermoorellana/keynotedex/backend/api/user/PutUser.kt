package es.guillermoorellana.keynotedex.backend.api.user

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.api.doUserProfileResponse
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.sessions.SessionStorage
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.backend.data.users.toDao
import es.guillermoorellana.keynotedex.datasource.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.datasource.responses.ErrorResponse
import es.guillermoorellana.keynotedex.datasource.responses.UserProfileResponse
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import io.ktor.routing.contentType

@UseExperimental(KtorExperimentalLocationsAPI::class)
fun Route.putUser(userStorage: UserStorage, sessionStorage: SessionStorage) {

    JsonSerializableConverter.register(UserProfileUpdateRequest.serializer())
    JsonSerializableConverter.register(UserProfileResponse.serializer())

    accept(ContentType.Application.Json) {
        contentType(ContentType.Application.Json) {
            authenticate {
                put<UserEndpoint> {
                    val request: UserProfileUpdateRequest = call.receive()
                    val sessionUser = getCurrentLoggedUser(userStorage)
                    if (request.user.userId != sessionUser?.userId) {
                        call.respond(HttpStatusCode.Forbidden, ErrorResponse("Nope"))
                    }
                    userStorage.updateUser(request.user.toDao())
                    val updatedUser = userStorage.retrieveUser(request.user.userId)!!
                    doUserProfileResponse(HttpStatusCode.Accepted, updatedUser, sessionStorage, sessionUser)
                }
            }
        }
    }
}
