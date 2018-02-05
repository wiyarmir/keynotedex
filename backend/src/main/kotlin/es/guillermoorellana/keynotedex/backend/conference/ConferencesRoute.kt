package es.guillermoorellana.keynotedex.backend.conference

import es.guillermoorellana.keynotedex.backend.*
import es.guillermoorellana.keynotedex.backend.dao.*
import es.guillermoorellana.keynotedex.backend.dao.tables.*
import es.guillermoorellana.keynotedex.responses.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.conference(storage: KeynotedexStorage) {
    accept(ContentType.Application.Json) {
        get<ConferencePage> {
            val conference = storage.conference(it.conferenceId)
            when (conference) {
                null -> call.respond(ErrorResponse("Can't find conference with id ${it.conferenceId}"))
                else -> call.respond(ConferenceResponse(conference.toDto()))
            }
        }
    }
}

