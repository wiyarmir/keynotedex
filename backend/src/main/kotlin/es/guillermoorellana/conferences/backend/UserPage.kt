package es.guillermoorellana.conferences.backend

import es.guillermoorellana.conferences.backend.dao.ConferencesStorage
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

fun Route.userPage(dao: ConferencesStorage) {
    get<UserPage> {
        val viewUser = dao.user(it.user)

        if (viewUser == null) {
            call.respond(HttpStatusCode.NotFound, "User ${it.user} doesn't exist")
        } else {
            call.respond("User: $viewUser")
        }
    }
}
