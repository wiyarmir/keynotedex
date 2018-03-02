package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.dao.KeynotedexDatabase
import es.guillermoorellana.keynotedex.backend.dao.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.index.index
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.error
import java.io.File

data class Session(val userId: String)

fun Application.keynotedex() {
    val storage: KeynotedexStorage = KeynotedexDatabase(File("build/db"))
        .apply {
            environment.log.warn("Populating db with mock data")
            mockData(this@keynotedex)
        }

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

    install(Sessions) {
        cookie<Session>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(sessionKey()))
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }

    install(Routing) {
        api(storage)
        index()
    }
}
