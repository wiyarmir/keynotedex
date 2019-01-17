package es.guillermoorellana.keynotedex.backend.api

import es.guillermoorellana.keynotedex.backend.ConferenceEndpoint
import es.guillermoorellana.keynotedex.backend.data.conferences.ConferenceStorage
import es.guillermoorellana.keynotedex.backend.data.conferences.toDto
import es.guillermoorellana.keynotedex.responses.ConferenceResponse
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.GetConference(conferenceStorage: ConferenceStorage) {
    accept(ContentType.Application.Json) {
        get<ConferenceEndpoint> { endpoint ->
            endpoint.conferenceId?.let { conferenceId ->
                val conference = conferenceStorage.conference(conferenceId)
                when (conference) {
                    null -> call.respond(ErrorResponse("Can't find conference with id ${endpoint.conferenceId}"))
                    else -> call.respond(ConferenceResponse(conference.toDto()))
                }
            }
        }
    }
}
