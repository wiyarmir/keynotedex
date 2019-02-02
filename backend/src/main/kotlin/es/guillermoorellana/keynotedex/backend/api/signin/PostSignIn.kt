package es.guillermoorellana.keynotedex.backend.api.signin

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.Session
import es.guillermoorellana.keynotedex.backend.api.isValidUserId
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.backend.hashPassword
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import es.guillermoorellana.keynotedex.responses.LoginResponse
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.post
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import io.ktor.sessions.sessions
import io.ktor.sessions.set

fun Route.PostSignIn(userStorage: UserStorage) {

    JsonSerializableConverter.register(LoginResponse.serializer())

    accept(ContentType.Application.Json) {
        post<SignInEndpoint> {
            val params = call.receiveParameters()
            val userId = params["userId"] ?: ""
            val password = params["password"] ?: ""

            val login = when {
                userId.length < 4 -> null
                password.length < 6 -> null
                !userId.isValidUserId() -> null
                else -> userStorage.retrieveUser(userId, application.hashPassword(password))
            }

            when (login) {
                null -> call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(message = "Invalid username or password")
                )
                else -> {
                    call.sessions.set(Session(login.userId))
                    call.respond(LoginResponse(login.toDto()))
                }
            }
        }
    }
}
