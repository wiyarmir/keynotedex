package es.guillermoorellana.keynotedex.backend

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Application.configureOAuth(authConf: Authentication.Configuration) = authConf.oauth("oauth") {
    val config = environment.config.config("keynotedex.oauth.github")
    client = HttpClient(Apache).apply { environment.monitor.subscribe(ApplicationStopping) { close() } }
    providerLookup = {
        OAuthServerSettings.OAuth2ServerSettings(
            name = "github",
            authorizeUrl = "https://github.com/login/oauth/authorize",
            accessTokenUrl = "https://github.com/login/oauth/access_token",
            clientId = config.propertyOrNull("clientId")?.getString() ?: "",
            clientSecret = config.propertyOrNull("clientSecret")?.getString() ?: ""
        )
    }
    urlProvider = { settings: OAuthServerSettings -> redirectString(OAuthLoginEndpoint(settings.name)) }
}

fun Routing.oauth() {
    authenticate("oauth") {
        location<OAuthLoginEndpoint> {
            param("error") {
                handle {
                    call.redirect(OauthFailedPage(call.parameters.getAll("error").orEmpty()))
                }
            }
            handle {
                val principal = call.principal<OAuthAccessTokenResponse>()
                when (principal) {
                    is OAuthAccessTokenResponse.OAuth2 -> call.sessions.set(Session(principal.accessToken))
                }
                call.redirect(LoginPage())
            }
        }
    }
}
