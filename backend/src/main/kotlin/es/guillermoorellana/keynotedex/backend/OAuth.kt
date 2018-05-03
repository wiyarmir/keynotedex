package es.guillermoorellana.keynotedex.backend

import io.ktor.application.Application
import io.ktor.application.ApplicationStopping
import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.locations.location
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.param

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
    urlProvider = { _: OAuthServerSettings -> "http://localhost:8080/oalogin" }
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
                val principal = call.authentication.principal<OAuthAccessTokenResponse>()
                if (principal != null) {
                    call.respondText {
                        "Success $principal"
                    }
                } else {
                    call.respond("login")
                }
            }
        }
    }
}
