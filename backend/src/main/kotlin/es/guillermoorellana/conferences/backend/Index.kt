package es.guillermoorellana.conferences.backend

import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.ContentType
import io.ktor.locations.get
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.index() {
    accept(ContentType.Text.Html) {
        get<Index> {
            call.respondHtmlTemplate(ApplicationPage()) {
                caption { +"Conferences" }
            }
        }
    }
}
