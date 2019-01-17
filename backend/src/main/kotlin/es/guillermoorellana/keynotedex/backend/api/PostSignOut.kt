package es.guillermoorellana.keynotedex.backend.api

import es.guillermoorellana.keynotedex.backend.LogoutEndpoint
import es.guillermoorellana.keynotedex.backend.Session
import es.guillermoorellana.keynotedex.responses.LogoutResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import io.ktor.sessions.sessions
import io.ktor.sessions.set

fun Route.PostSignOut() {
    accept(ContentType.Application.Json) {
        post<LogoutEndpoint> {
            call.sessions.set<Session>(null)
            call.respond(LogoutResponse())
        }
    }
}
