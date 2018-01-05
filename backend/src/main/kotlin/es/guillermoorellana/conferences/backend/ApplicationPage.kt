package es.guillermoorellana.conferences.backend

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.*

class ApplicationPage : Template<HTML> {
    val caption = Placeholder<TITLE>()
    val head = Placeholder<HEAD>()

    override fun HTML.apply() {
        head {
            meta { charset = "utf-8" }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1.0"
            }
            title {
                insert(caption)
            }
            insert(head)
//            link(rel = LinkRel.stylesheet, type = LinkType.textCss, href = "http://yui.yahooapis.com/pure/0.6.0/pure-min.css")
        }
        body {
            div { id = "content" }
            script(src = "frontend/frontend.bundle.js")
        }
    }
}
