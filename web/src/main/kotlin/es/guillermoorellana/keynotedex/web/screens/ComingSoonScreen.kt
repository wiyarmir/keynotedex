package es.guillermoorellana.keynotedex.web.screens

import react.*
import react.dom.*

class ComingSoonScreen : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        style {
            //language=CSS
            +".error-template {padding: 40px 15px;text-align: center;}"
        }
        div("row") {
            div("col-md-12") {
                div("error-template") {
                    h1 { +"Coming soon!" }
                    h2 { +"Not part of the MVP :)" }
                }
            }
        }
    }
}
