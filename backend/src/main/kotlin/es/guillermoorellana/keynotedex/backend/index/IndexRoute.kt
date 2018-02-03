package es.guillermoorellana.keynotedex.backend.index

import es.guillermoorellana.keynotedex.backend.*
import es.guillermoorellana.keynotedex.backend.dao.*
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.routing.*

fun Route.index(storage: KeynotedexStorage) {
    accept(ContentType.Text.Html) {
        get<Index> {
            call.respondHtmlTemplate(ApplicationPageContent()) {
                caption { +"Keynotedex" }
            }
        }
    }
}

