package es.guillermoorellana.keynotedex.backend

import io.ktor.locations.*

@location("/{trail...}")
class Index(val trail: List<String>)

@location("/user/{userId}")
data class UserPage(val userId: String)

@location("/user/{userId}/submissionById/{submissionId?}")
data class SubmissionPage(val userId: String, val submissionId: String?)

@location("/conference/{conferenceId}")
data class ConferencePage(val conferenceId: String)

@location("/register")
data class RegisterPage(val userId: String = "", val password: String = "")

@location("/login")
data class LoginPage(val userId: String = "", val password: String = "")
