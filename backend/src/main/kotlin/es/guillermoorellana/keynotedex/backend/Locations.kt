package es.guillermoorellana.keynotedex.backend

import io.ktor.locations.*

@Location("/{trail...}")
class IndexPage(val trail: List<String>)

@Location("/user/{userId}")
data class UserEndpoint(val userId: String)

@Location("/submission/{submissionId}")
data class SubmissionEndpoint(val submissionId: String)

@Location("/conference/{conferenceId}")
data class ConferenceEndpoint(val conferenceId: String)

@Location("/register")
data class RegisterEndpoint(val userId: String = "", val password: String = "")

@Location("/login")
data class LoginEndpoint(val userId: String = "", val password: String = "")
