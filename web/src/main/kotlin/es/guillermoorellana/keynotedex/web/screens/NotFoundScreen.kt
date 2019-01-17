package es.guillermoorellana.keynotedex.web.screens

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.h1
import react.dom.h2
import react.dom.style

class NotFoundScreen : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        style {
            // language=CSS
            +".error-template {padding: 40px 15px;text-align: center;}"
        }
        div("row") {
            div("col-md-12") {
                div("error-template") {
                    h1 { +"Oops!" }
                    h2 { +"404 Not Found" }
                }
            }
        }
    }
}
