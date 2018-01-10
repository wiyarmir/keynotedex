package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.dao.ConferencesDatabase
import es.guillermoorellana.keynotedex.backend.index.index
import es.guillermoorellana.keynotedex.backend.user.userPage
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.PartialContentSupport
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie

data class Session(val userId: String)

fun Application.main() {
    val storage = ConferencesDatabase()

    install(DefaultHeaders)
    install(CallLogging)
    install(ConditionalHeaders)
    install(PartialContentSupport)
    install(Compression)
    install(Locations)
    install(StatusPages) {
        exception<NotImplementedError> { call.respond(HttpStatusCode.NotImplemented) }
    }

    install(Sessions) {
        cookie<Session>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    install(ContentNegotiation) {
        gson {

        }
    }

    install(Routing) {
        index(storage)
        userPage(storage)
    }
}

