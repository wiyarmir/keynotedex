package es.guillermoorellana.keynotedex.backend.api.sessions

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.api.getCurrentLoggedUser
import es.guillermoorellana.keynotedex.backend.data.sessions.SessionStorage
import es.guillermoorellana.keynotedex.backend.data.sessions.toDao
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.datasource.requests.SessionCreateRequest
import es.guillermoorellana.keynotedex.datasource.responses.ErrorResponse
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import java.sql.SQLException

@UseExperimental(KtorExperimentalLocationsAPI::class)
fun Route.postSession(sessionStorage: SessionStorage, userStorage: UserStorage) {

    JsonSerializableConverter.register(SessionCreateRequest.serializer())

    accept(ContentType.Application.Json) {
        authenticate {
            post<SessionsEndpoint> {
                val user = getCurrentLoggedUser(userStorage)
                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }

                val incompleteSession = call.receive<SessionCreateRequest>()
                val session = incompleteSession.toDao(userId = user.userId)
                try {
                    sessionStorage.create(session)
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
