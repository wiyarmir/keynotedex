package es.guillermoorellana.keynotedex.backend.api.conference

import es.guillermoorellana.keynotedex.api.Api
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@UseExperimental(KtorExperimentalLocationsAPI::class)
@Location(Api.V1.Paths.conferences)
data class ConferenceEndpoint(val conferenceId: String? = null)
