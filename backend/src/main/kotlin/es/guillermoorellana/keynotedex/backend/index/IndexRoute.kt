package es.guillermoorellana.keynotedex.backend.index

import es.guillermoorellana.keynotedex.backend.*
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.routing.*

fun Route.index() {
    accept(ContentType.Text.Html) {
        get<Index> {
            call.respondHtmlTemplate(ApplicationPageContent()) {
                caption { +"Keynotedex" }
            }
        }
    }
}

