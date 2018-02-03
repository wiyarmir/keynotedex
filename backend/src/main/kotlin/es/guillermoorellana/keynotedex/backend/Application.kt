package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.conference.*
import es.guillermoorellana.keynotedex.backend.dao.*
import es.guillermoorellana.keynotedex.backend.index.*
import es.guillermoorellana.keynotedex.backend.login.*
import es.guillermoorellana.keynotedex.backend.register.*
import es.guillermoorellana.keynotedex.backend.submission.*
import es.guillermoorellana.keynotedex.backend.user.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*

data class Session(val userId: String)

fun Application.main() {
    val storage: KeynotedexStorage = KeynotedexDatabase().apply {
        environment.log.debug("Populating db with mock data")
        mockData()
    }

    install(DefaultHeaders) {
        //        header(HttpHeaders.Server, "")
    }
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
        login(storage)
        submission(storage)
    }
}
