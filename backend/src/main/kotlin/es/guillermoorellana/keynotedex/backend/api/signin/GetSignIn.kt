package es.guillermoorellana.keynotedex.backend.api.signin

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.SignInEndpoint
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import es.guillermoorellana.keynotedex.responses.LoginResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.GetSignIn(userStorage: UserStorage) {

    JsonSerializableConverter.register(LoginResponse.serializer())

    accept(ContentType.Application.Json) {
        get<SignInEndpoint> {
            val user = getCurrentLoggedUser(userStorage)
            when (user) {
                null -> call.respond(HttpStatusCode.Forbidden, ErrorResponse("Forbidden"))
                else -> call.respond(LoginResponse(user.toDto()))
            }
        }
    }
}
