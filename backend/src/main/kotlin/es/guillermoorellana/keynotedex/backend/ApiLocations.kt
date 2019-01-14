package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.api.Api
import io.ktor.locations.Location

@Location(Api.V1.Paths.user)
data class UserEndpoint(val userId: String)

@Location(Api.V1.Paths.submissions)
data class SubmissionsEndpoint(val submissionId: String)

@Location(Api.V1.Paths.conferences)
data class ConferenceEndpoint(val conferenceId: String?)

@Location(Api.V1.Paths.register)
data class RegisterEndpoint(val userId: String = "", val password: String = "")

@Location(Api.V1.Paths.login)
data class LoginEndpoint(val userId: String = "", val password: String = "")

@Location(Api.V1.Paths.logout)
class LogoutEndpoint
