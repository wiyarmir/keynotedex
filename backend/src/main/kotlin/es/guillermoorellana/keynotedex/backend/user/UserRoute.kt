package es.guillermoorellana.keynotedex.backend.user

import es.guillermoorellana.keynotedex.backend.*
import es.guillermoorellana.keynotedex.backend.dao.*
import es.guillermoorellana.keynotedex.backend.error.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.user(dao: KeynotedexStorage) {
    accept(ContentType.Application.Json) {
        get<UserPage> {
            val user = dao.user(it.userId)
            when (user) {
                null -> call.respond(HttpStatusCode.NotFound, ErrorResponse("User ${it.userId} doesn't exist"))
                else -> {
                    val submissions = dao.submissionsByUserId(user.userId)
                    call.respond(UserResponse(user.copy(submissions = submissions).toPublic()))
                }
            }
        }
    }
}
