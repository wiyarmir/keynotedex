package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.conferences.conference
import es.guillermoorellana.keynotedex.backend.dao.*
import es.guillermoorellana.keynotedex.backend.index.index
import es.guillermoorellana.keynotedex.backend.user.register.register
import es.guillermoorellana.keynotedex.backend.user.user
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.*
import io.ktor.util.error

data class Session(val userId: String)

fun Application.main() {
    val storage: KeynotedexStorage = KeynotedexDatabase().apply {
        environment.log.debug("Populating db with mock data")
        mockData()
    }

    install(DefaultHeaders)
    install(CallLogging)
    install(ConditionalHeaders)
    install(PartialContentSupport)
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
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    install(ContentNegotiation) {
        gson { }
    }

    install(Routing) {
        index(storage)
        user(storage)
        conference(storage)
        register(storage, ::hash)
    }
}
