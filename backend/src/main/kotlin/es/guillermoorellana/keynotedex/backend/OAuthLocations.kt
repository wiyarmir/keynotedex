package es.guillermoorellana.keynotedex.backend

import io.ktor.locations.Location

@Location("/login/{service}")
data class OAuthLoginEndpoint(val service: String)

@Location("/oauth_failed")
data class OauthFailedPage(val parameters: List<String>)
