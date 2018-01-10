package es.guillermoorellana.keynotedex.backend

import io.ktor.locations.location

@location("/")
class Index()

@location("/userId/{userId}")
data class UserPage(val userId: String)

@location("/conference/{conferenceId}")
data class ConferencePage(val conferenceId: String)
