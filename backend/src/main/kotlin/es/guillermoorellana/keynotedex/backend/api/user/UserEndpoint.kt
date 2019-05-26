package es.guillermoorellana.keynotedex.backend.api.user

import es.guillermoorellana.keynotedex.api.Api
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@UseExperimental(KtorExperimentalLocationsAPI::class)
@Location(Api.V1.Paths.user)
data class UserEndpoint(val userId: String)
