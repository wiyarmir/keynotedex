package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.data.*
import freemarker.cache.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import java.io.*

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

    notInProduction { install(Authentication) { configureOAuth(this) } }

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
        notInProduction { oauth() }
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

fun Application.notInProduction(block: () -> Unit) {
    if (isDevelopment()) block()
}
