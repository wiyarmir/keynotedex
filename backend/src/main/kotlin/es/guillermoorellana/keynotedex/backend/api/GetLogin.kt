package es.guillermoorellana.keynotedex.backend.api

import es.guillermoorellana.keynotedex.backend.LoginEndpoint
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

fun Route.GetLogin(userStorage: UserStorage) {
    accept(ContentType.Application.Json) {
        get<LoginEndpoint> {
            val user = getCurrentLoggedUser(userStorage)
            when (user) {
                null -> call.respond(
                    HttpStatusCode.Forbidden,
                    ErrorResponse("Forbidden")
                )
                else -> call.respond(LoginResponse(user.toDto()))
            }
        }
    }
}
