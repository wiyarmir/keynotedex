package es.guillermoorellana.keynotedex.backend

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.HEAD
import kotlinx.html.HTML
import kotlinx.html.LinkRel
import kotlinx.html.LinkType
import kotlinx.html.TITLE
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.title

class ApplicationPageContent : Template<HTML> {
    val caption = Placeholder<TITLE>()
    val head = Placeholder<HEAD>()

    override fun HTML.apply() {
        head {
            meta { charset = "utf-8" }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1.0, shrink-to-fit=no"
            }
            title {
                insert(caption)
            }
            insert(head)
            link(rel = LinkRel.stylesheet, type = LinkType.textCss, href = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css")
        }
        body {
            div { id = "content" }

            script(src = "https://code.jquery.com/jquery-3.2.1.slim.min.js")
            script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js")
            script(src = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js")

            script(src = "frontend/web.bundle.js")
        }
    }
}
