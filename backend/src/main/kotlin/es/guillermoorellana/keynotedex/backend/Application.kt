package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.api.api
import es.guillermoorellana.keynotedex.backend.auth.JwtConfig
import es.guillermoorellana.keynotedex.backend.auth.JwtTokenProvider
import es.guillermoorellana.keynotedex.backend.data.KeynotedexDatabase
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.PartialContent
import io.ktor.features.StatusPages
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.accept
import io.ktor.util.error
import java.io.File

class Keynotedex

fun Application.keynotedex(
    storage: KeynotedexStorage = createStorage(),
    jwtConfig: JwtConfig = createJwtConfig(),
    jwtTokenProvider: JwtTokenProvider = createJwtTokenProvider(jwtConfig)
) {

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
        jwt {
            jwtConfig.applyJwtConfig(this)
        }
    }

    install(ContentNegotiation) {
        serializable {
            register(ErrorResponse.serializer())
        }
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(Keynotedex::class.java.classLoader, "")
    }

    install(Routing) {
        api(storage, jwtTokenProvider)
        index()
    }
}

fun createJwtTokenProvider(jwtConfig: JwtConfig): JwtTokenProvider = { userId -> jwtConfig.makeToken(userId) }

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

fun Application.createJwtConfig() = JwtConfig(environment)

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
