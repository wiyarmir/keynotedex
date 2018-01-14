package es.guillermoorellana.keynotedex.backend.user.register

import es.guillermoorellana.keynotedex.backend.RegisterPage
import es.guillermoorellana.keynotedex.backend.Session
import es.guillermoorellana.keynotedex.backend.dao.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.error.ErrorResponse
import es.guillermoorellana.keynotedex.backend.redirect
import es.guillermoorellana.keynotedex.backend.user.UserResponse
import es.guillermoorellana.keynotedex.backend.user.model.User
import es.guillermoorellana.keynotedex.backend.user.model.toPublic
import es.guillermoorellana.keynotedex.backend.userNameValid
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.ValuesMap

fun Route.register(dao: KeynotedexStorage, hashFunction: (String) -> String) {
    post<RegisterPage> {
        val user = call.sessions.get<Session>()?.let { dao.user(it.userId) }
        if (user != null) {
            call.redirect(UserResponse(user.toPublic()))
            return@post
        }

        val form = call.receive<ValuesMap>()
        val userId = form["userId"] ?: ""
        val password = form["password"] ?: ""
        val displayName = form["displayName"] ?: ""
        val email = form["email"] ?: ""

        when {
            passwordNotValid(password) -> {
                call.respond(HttpStatusCode.PreconditionFailed, ErrorResponse(message = "Password should be at least 6 characters long"))
                return@post
            }
            userIdShort(userId) -> {
                call.respond(HttpStatusCode.PreconditionFailed, ErrorResponse(message = "Login should be at least 4 characters long"))
                return@post
            }
            userNameNotValid(userId) -> {
                call.respond(HttpStatusCode.PreconditionFailed, ErrorResponse(message = "Login should be consists of digits, letters, dots or underscores"))
                return@post
            }
            emailNotValid(email) -> {
                call.respond(HttpStatusCode.PreconditionFailed, ErrorResponse(message = "Email is empty or not valid"))
                return@post
            }
        }

        val hash = hashFunction(password)
        val newUser = User(userId, email, displayName, hash)

        try {
            dao.createUser(newUser)
        } catch (e: Throwable) {
            application.environment.log.error("Failed to register user", e)
            when {
                userInDatabase(dao, userId) -> call.respond(HttpStatusCode.Conflict, ErrorResponse(message = "User with the following login is already registered"))
                email.isNotEmpty() && dao.userByEmail(email) != null -> call.respond(HttpStatusCode.Conflict, ErrorResponse(message = "User with the following email $email is already registered"))
                else -> {
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(message = "Failed to register"))
                }
            }
            return@post
        }

        call.sessions.set(Session(newUser.userId))
        call.respond(UserResponse(newUser.toPublic()))
    }

    get<RegisterPage> {
        call.respond(HttpStatusCode.MethodNotAllowed)
    }
}

private fun passwordNotValid(password: String) = password.length < 6
private fun userIdShort(userId: String) = userId.length < 4
private fun userNameNotValid(userName: String) = !userNameValid(userName)
private fun userInDatabase(dao: KeynotedexStorage, userId: String) =
        dao.user(userId) != null

private fun emailNotValid(email: String): Boolean = email.isBlank()
