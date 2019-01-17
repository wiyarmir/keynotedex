@file:UseExperimental(KtorExperimentalLocationsAPI::class)

package es.guillermoorellana.keynotedex.backend

import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

@Location("/{trail...}")
data class IndexPage(val trail: List<String>)

@Location("/login")
class LoginPage
