package es.guillermoorellana.keynotedex.backend.api.signup

import es.guillermoorellana.keynotedex.backend.Session
import es.guillermoorellana.keynotedex.backend.SignUpEndpoint
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.api.isValidUserId
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.backend.hashPassword
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import es.guillermoorellana.keynotedex.responses.UserProfileResponse
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.h2.message.DbException

fun Route.PostSignUp(userStorage: UserStorage) {

    fun passwordNotValid(password: String?) = password?.let { it.length < 6 } ?: true
    fun userIdShort(userId: String?) = userId?.let { it.length < 4 } ?: true
    fun userNameNotValid(userName: String?) = userName?.isValidUserId()?.not() ?: true
    fun userInDatabase(dao: UserStorage, userId: String?) = userId?.let { dao.retrieveUser(it) != null } ?: false

    post<SignUpEndpoint> {
        val user = getCurrentLoggedUser(userStorage)
        if (user != null) {
            call.respond(
                HttpStatusCode.MethodNotAllowed,
                ErrorResponse(message = "You are already logged in")
            )
            return@post
        }
        val params = call.receive<Parameters>()
        val userId = params["userId"]
        val password = params["password"]
        val email = params["email"]
        val displayName = params["displayName"]

        when {
            passwordNotValid(password) -> {
                call.respond(
                    HttpStatusCode.PreconditionFailed,
                    ErrorResponse(message = "Password should be at least 6 characters long")
                )
                return@post
            }
            userIdShort(userId) -> {
                call.respond(
                    HttpStatusCode.PreconditionFailed,
                    ErrorResponse(message = "Login should be at least 4 characters long")
                )
                return@post
            }
            userNameNotValid(userId) -> {
                call.respond(
                    HttpStatusCode.PreconditionFailed,
                    ErrorResponse(message = "Login should consist of digits, letters, dots or underscores")
                )
                return@post
            }
            userInDatabase(userStorage, userId) -> {
                call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse(message = "User with the following login is already registered")
                )
                return@post
            }
        }

        val hash = application.hashPassword(requireNotNull(password))
        val newUser = User(
            userId = requireNotNull(userId),
            passwordHash = hash,
            displayName = displayName,
            email = email
        )

        try {
            userStorage.createUser(newUser)
        } catch (e: DbException) {
            application.environment.log.error("Failed to register user", e)
            when {
                userInDatabase(userStorage, userId) -> call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse(message = "User with the following login is already registered")
                )
                else -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = "Failed to register")
                )
            }
            return@post
        }

        call.sessions.set(Session(newUser.userId))
        call.respond(HttpStatusCode.Created, UserProfileResponse(newUser.toDto()))
    }
}