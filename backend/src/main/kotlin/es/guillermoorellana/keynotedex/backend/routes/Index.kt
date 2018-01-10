package es.guillermoorellana.keynotedex.backend.routes

import es.guillermoorellana.keynotedex.backend.Index
import es.guillermoorellana.keynotedex.backend.dao.ConferencesDatabase
import es.guillermoorellana.keynotedex.backend.model.responses.IndexResponse
import es.guillermoorellana.keynotedex.backend.pages.ApplicationPage
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
                caption { +"Conferences" }
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
