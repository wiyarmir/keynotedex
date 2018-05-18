package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.dao.KeynotedexDatabase
import es.guillermoorellana.keynotedex.backend.dao.KeynotedexStorage
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.content.resource
import io.ktor.content.static
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.accept
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.error
import java.io.File

class Keynotedex

data class Session(val userId: String)

fun Application.keynotedex() {
    val storage: KeynotedexStorage = createStorage()

    install(DefaultHeaders)
    install(CallLogging)
    install(ConditionalHeaders)
    install(PartialContent)
    install(Compression)
    install(Locations)
    install(StatusPages) {
        exception<Throwable> { cause ->
            environment.log.error(cause)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    install(Authentication) {
        configureOAuth(this)
    }

    install(Sessions) {
        cookie<Session>("SESSION") {
            cookie.path = "/"
            transform(SessionTransportTransformerMessageAuthentication(sessionKey()))
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(Keynotedex::class.java.classLoader, "")
    }

    install(Routing) {
        oauth()
        api(storage)
        index()
    }
}

private fun Route.index() {
    static("frontend") {
        resource("web.bundle.js")
    }
    accept(ContentType.Text.Html) {
        get<IndexPage> {
            val model = mapOf(
                "jsbundle" to "/frontend/web.bundle.js"
            )
            call.respond(FreeMarkerContent(template = "index.ftl", model = model))
        }
    }
}

private fun Application.createStorage(): KeynotedexDatabase =
    when {
        isDevelopment() -> KeynotedexDatabase(File("build/db"))
        else -> KeynotedexDatabase()
    }.apply {
        environment.log.warn("Populating db with mock data")
        mockData(this@createStorage)
    }

fun Application.isDevelopment() =
    environment.config.propertyOrNull("ktor.deployment.environment")?.getString() == "development"
