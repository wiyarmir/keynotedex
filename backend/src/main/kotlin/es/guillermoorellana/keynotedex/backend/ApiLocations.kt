package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.api.Api
import io.ktor.locations.Location

@Location(Api.V1.Paths.user)
data class UserEndpoint(val userId: String)

@Location(Api.V1.Paths.submissions)
data class SubmissionsEndpoint(val submissionId: String? = null)

@Location(Api.V1.Paths.conferences)
data class ConferenceEndpoint(val conferenceId: String? = null)

@Location(Api.V1.Paths.register)
class SignUpEndpoint

@Location(Api.V1.Paths.login)
class SignInEndpoint

@Location(Api.V1.Paths.logout)
class SignOutEndpoint
