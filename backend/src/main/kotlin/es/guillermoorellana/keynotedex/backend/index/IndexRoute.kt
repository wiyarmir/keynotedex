package es.guillermoorellana.keynotedex.backend.index

import es.guillermoorellana.keynotedex.backend.ApplicationPageContent
import es.guillermoorellana.keynotedex.backend.Index
import es.guillermoorellana.keynotedex.backend.dao.KeynotedexStorage
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.ContentType
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.index(storage: KeynotedexStorage) {
    accept(ContentType.Text.Html) {
        get<Index> {
            call.respondHtmlTemplate(ApplicationPageContent()) {
                caption { +"Keynotedex" }
            }
        }
    }
    accept(ContentType.Application.Json) {
        get<Index> {
            val conferences = storage.conferences()
            call.respond(IndexResponse(conferences))
        }
    }
}
