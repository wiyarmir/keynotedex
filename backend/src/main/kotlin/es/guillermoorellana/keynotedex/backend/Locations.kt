package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.api.ApiPaths
import io.ktor.locations.Location

@Location("/{trail...}")
class IndexPage(val trail: List<String>)

@Location("/login/{service}")
data class OAuthLoginEndpoint(val service: String)

@Location("/oauth_failed")
data class OauthFailedPage(val parameters: List<String>)

@Location(ApiPaths.user)
data class UserEndpoint(val userId: String)

@Location(ApiPaths.submissions)
data class SubmissionsEndpoint(val submissionId: String)

@Location(ApiPaths.conferences)
data class ConferenceEndpoint(val conferenceId: String?)

@Location(ApiPaths.register)
data class RegisterEndpoint(val userId: String = "", val password: String = "")

@Location(ApiPaths.login)
data class LoginEndpoint(val userId: String = "", val password: String = "")

@Location(ApiPaths.logout)
class LogoutEndpoint

@Location("/login")
class LoginPage
