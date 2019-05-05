package es.guillermoorellana.keynotedex.backend.api.sessions

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.sessions.SessionStorage
import es.guillermoorellana.keynotedex.backend.data.sessions.toDao
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.datasource.requests.SessionUpdateRequest
import es.guillermoorellana.keynotedex.datasource.responses.ErrorResponse
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import java.sql.SQLException

@UseExperimental(KtorExperimentalLocationsAPI::class)
fun Route.putSession(sessionStorage: SessionStorage, userStorage: UserStorage) {

    JsonSerializableConverter.register(SessionUpdateRequest.serializer())

    accept(ContentType.Application.Json) {
        authenticate {
            put<SessionsEndpoint> {
                val user = getCurrentLoggedUser(userStorage)
                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@put
                }
                val session = call.receive<SessionUpdateRequest>().session
                if (session.userId != user.userId) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@put
                }
                try {
                    sessionStorage.update(session.toDao())
                } catch (e: SQLException) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse(message = "Failed to create session: ${e.message}")
                    )
                }
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
