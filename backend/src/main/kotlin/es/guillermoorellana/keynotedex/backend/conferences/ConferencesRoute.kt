package es.guillermoorellana.keynotedex.backend.conferences

import es.guillermoorellana.keynotedex.backend.ConferencePage
import es.guillermoorellana.keynotedex.backend.dao.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.error.ErrorResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.conference(storage: KeynotedexStorage) {
    accept(ContentType.Application.Json) {
        get<ConferencePage> {
            val conference = storage.conference(it.conferenceId)
            when (conference) {
                null -> call.respond(ErrorResponse("Can't find conference with id ${it.conferenceId}"))
                else -> call.respond(ConferenceResponse(conference))
            }
        }
    }
}

