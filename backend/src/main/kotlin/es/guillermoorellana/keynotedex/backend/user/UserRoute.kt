package es.guillermoorellana.keynotedex.backend.user

import es.guillermoorellana.keynotedex.backend.UserPage
import es.guillermoorellana.keynotedex.backend.dao.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.error.ErrorResponse
import es.guillermoorellana.keynotedex.backend.user.model.toPublic
import io.ktor.application.call
import io.ktor.http.*
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.user(dao: KeynotedexStorage) {
    accept(ContentType.Application.Json) {
        get<UserPage> {
            val user = dao.user(it.userId)
            when (user) {
                null -> call.respond(HttpStatusCode.NotFound, ErrorResponse("User ${it.userId} doesn't exist"))
                else -> call.respond(UserResponse(user.toPublic()))
            }
        }
    }
}
