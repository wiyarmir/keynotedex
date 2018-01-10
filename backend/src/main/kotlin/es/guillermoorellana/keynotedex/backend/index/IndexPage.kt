package es.guillermoorellana.keynotedex.backend.index

import es.guillermoorellana.keynotedex.backend.ApplicationPage
import es.guillermoorellana.keynotedex.backend.Index
import es.guillermoorellana.keynotedex.backend.dao.ConferencesDatabase
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.ContentType
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.index(storage: ConferencesDatabase) {
    accept(ContentType.Text.Html) {
        get<Index> {
            call.respondHtmlTemplate(ApplicationPage()) {
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
