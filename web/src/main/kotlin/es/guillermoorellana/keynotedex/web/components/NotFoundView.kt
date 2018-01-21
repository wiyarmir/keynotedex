package es.guillermoorellana.keynotedex.web.components

import react.*
import react.dom.*

class NotFoundView : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        style {
            //language=CSS
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
