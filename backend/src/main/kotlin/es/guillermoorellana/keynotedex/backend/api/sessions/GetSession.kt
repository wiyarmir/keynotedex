package es.guillermoorellana.keynotedex.backend.api.sessions

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.hashids
import es.guillermoorellana.keynotedex.backend.data.sessions.SessionStorage
import es.guillermoorellana.keynotedex.backend.data.sessions.toDto
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.datasource.responses.ErrorResponse
import es.guillermoorellana.keynotedex.datasource.responses.SessionResponse
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

@UseExperimental(KtorExperimentalLocationsAPI::class)
fun Route.getSession(sessionStorage: SessionStorage, userStorage: UserStorage) {

    JsonSerializableConverter.register(SessionResponse.serializer())

    accept(ContentType.Application.Json) {
        authenticate(optional = true) {
            get<SessionsEndpoint> { (sessionId) ->
                val session = sessionId
                    ?.let { hashids.decode(it).firstOrNull() }
                    ?.let { sessionStorage.getById(it) }
                if (session == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
                    return@get
                }
                if (session.isPublic) {
                    call.respond(SessionResponse(session.toDto()))
                    return@get
                }
                val user = getCurrentLoggedUser(userStorage)
                if (user?.userId == session.submitterId) {
                    call.respond(SessionResponse(session.toDto()))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
                }
            }
        }
    }
}
