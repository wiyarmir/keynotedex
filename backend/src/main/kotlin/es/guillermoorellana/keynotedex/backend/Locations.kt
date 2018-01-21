package es.guillermoorellana.keynotedex.backend

import io.ktor.locations.location

@location("/")
class Index()

@location("/user/{userId}")
data class UserPage(val userId: String)

@location("/conference/{conferenceId}")
data class ConferencePage(val conferenceId: String)

@location("/register")
data class RegisterPage(val userId: String = "", val password: String = "")

@location("/login")
data class LoginPage(val userId: String = "", val password: String = "")
