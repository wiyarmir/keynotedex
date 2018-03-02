package es.guillermoorellana.keynotedex.backend.index

import es.guillermoorellana.keynotedex.backend.IndexPage
import io.ktor.application.call
import io.ktor.content.resource
import io.ktor.content.static
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.ContentType
import io.ktor.locations.get
import io.ktor.routing.Route
import io.ktor.routing.accept

fun Route.index() {
    accept(ContentType.Text.Html) {
        get<IndexPage> {
            call.respondHtmlTemplate(IndexPageContent()) {
                caption { +"Keynotedex" }
            }
        }
    }
    static("frontend") {
        resource("web.bundle.js")
    }
}

