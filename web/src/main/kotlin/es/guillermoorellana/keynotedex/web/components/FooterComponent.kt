package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.external.routeLink
import react.RBuilder
import react.dom.a
import react.dom.footer
import react.dom.hr
import react.dom.p
import react.dom.style

// language=CSS
private val css =
    """
    footer {
        margin-top: 2em;
    }
    """.trimIndent()

fun RBuilder.appFooter() {
    style { +css }
    footer("container") {
        hr { }
        p {
            +"Keynotedex, an "
            a(href = "https://github.com/wiyarmir/keynotedex", target = "blank") { +"OpenSource" }
            +" initiative by "
            a(href = "https://guillermoorellana.es", target = "blank") { +"Guillermo Orellana" }
            +" — "
            routeLink(to = "/terms") { +"Terms of Service" }
            +" — "
            routeLink(to = "/privacy") { +"Privacy Policy" }
        }
    }
}
