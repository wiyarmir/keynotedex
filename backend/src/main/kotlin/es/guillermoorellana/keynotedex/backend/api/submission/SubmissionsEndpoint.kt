package es.guillermoorellana.keynotedex.backend.api.submission

import es.guillermoorellana.keynotedex.api.Api
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@UseExperimental(KtorExperimentalLocationsAPI::class)
@Location(Api.V1.Paths.submissions)
data class SubmissionsEndpoint(val submissionId: String? = null)
