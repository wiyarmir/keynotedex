package es.guillermoorellana.keynotedex.backend.api.conference

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.backend.data.conferences.ConferenceStorage
import es.guillermoorellana.keynotedex.backend.data.conferences.toDto
import es.guillermoorellana.keynotedex.backend.data.hashids
import es.guillermoorellana.keynotedex.datasource.responses.ConferenceResponse
import es.guillermoorellana.keynotedex.datasource.responses.ConferencesResponse
import es.guillermoorellana.keynotedex.datasource.responses.ErrorResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

@UseExperimental(KtorExperimentalLocationsAPI::class)
fun Route.getConference(conferenceStorage: ConferenceStorage) {

    JsonSerializableConverter.register(ConferenceResponse.serializer())
    JsonSerializableConverter.register(ConferencesResponse.serializer())

    accept(ContentType.Application.Json) {
        get<ConferenceEndpoint> { (conferenceId) ->
            if (conferenceId == null) {
                call.respond(ConferencesResponse(conferenceStorage.conferences().toDto()))
                return@get
            }
            val conference = conferenceId
                .let { hashids.decode(it).firstOrNull() }
                ?.let { conferenceStorage.conference(it) }
            when (conference) {
                null -> call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
                else -> call.respond(ConferenceResponse(conference.toDto()))
            }
        }
    }
}
