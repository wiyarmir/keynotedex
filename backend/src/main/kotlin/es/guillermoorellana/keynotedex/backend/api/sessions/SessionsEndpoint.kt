package es.guillermoorellana.keynotedex.backend.api.sessions

import es.guillermoorellana.keynotedex.api.Api
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@UseExperimental(KtorExperimentalLocationsAPI::class)
@Location(Api.V1.Paths.sessions)
data class SessionsEndpoint(val sessionId: String? = null)
