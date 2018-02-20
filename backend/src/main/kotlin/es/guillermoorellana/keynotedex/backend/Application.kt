package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.dao.*
import es.guillermoorellana.keynotedex.backend.index.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import java.io.*

data class Session(val userId: String)

fun Application.main() {
    val storage: KeynotedexStorage = KeynotedexDatabase(File("build/db"))
        .apply {
            environment.log.warn("Populating db with mock data")
            mockData()
        }

    install(DefaultHeaders) {
        //        header(HttpHeaders.Server, "")
    }
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
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }

    install(Routing) {
        index()
        api(storage, ::hash)
    }
}
