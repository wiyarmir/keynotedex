package es.guillermoorellana.keynotedex.backend

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.HEAD
import kotlinx.html.HTML
import kotlinx.html.TITLE
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.title

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
