package es.guillermoorellana.keynotedex.backend

import io.ktor.locations.*

@location("/{trail...}")
class IndexPage(val trail: List<String>)

@location("/user/{userId}")
data class UserEndpoint(val userId: String)

@location("/submission/{submissionId}")
data class SubmissionEndpoint(val submissionId: String)

@location("/conference/{conferenceId}")
data class ConferenceEndpoint(val conferenceId: String)

@location("/register")
data class RegisterEndpoint(val userId: String = "", val password: String = "")

@location("/login")
data class LoginEndpoint(val userId: String = "", val password: String = "")
