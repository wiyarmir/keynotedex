package es.guillermoorellana.keynotedex.backend.api.signout

import es.guillermoorellana.keynotedex.api.Api
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@UseExperimental(KtorExperimentalLocationsAPI::class)
@Location(Api.V1.Paths.signOut)
class SignOutEndpoint
