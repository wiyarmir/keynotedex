package es.guillermoorellana.keynotedex.backend

import io.ktor.locations.location

@location("/")
class Index()

@location("/user/{userId}")
data class UserPage(val userId: String)

@location("/conference/{conferenceId}")
data class ConferencePage(val conferenceId: String)

@location("/register")
data class RegisterPage(
        val userId: String = "",
        val displayName: String = "",
        val email: String = "",
        val password: String = "",
        val error: String = ""
)
