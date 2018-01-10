package es.guillermoorellana.keynotedex.backend.routes

import es.guillermoorellana.keynotedex.backend.UserPage
import es.guillermoorellana.keynotedex.backend.dao.ConferencesStorage
import es.guillermoorellana.keynotedex.backend.model.responses.ErrorResponse
import es.guillermoorellana.keynotedex.backend.model.responses.UserResponse
import es.guillermoorellana.keynotedex.backend.model.toPublic
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.userPage(dao: ConferencesStorage) {
    accept(ContentType.Text.Html) {
        get<UserPage> {
            val user = dao.user(it.user)
            when (user) {
                null -> call.respond(HttpStatusCode.NotFound, "User ${it.user} doesn't exist")
                else -> call.respond("User: $user")
            }
        }
    }
    accept(ContentType.Application.Json) {
        get<UserPage> {
            val user = dao.user(it.user)
            when (user) {
                null -> call.respond(ErrorResponse("User ${it.user} doesn't exist"))
                else -> call.respond(UserResponse(user.toPublic()))
            }
        }
    }
}
