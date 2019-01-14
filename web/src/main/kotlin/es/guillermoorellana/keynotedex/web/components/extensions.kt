package es.guillermoorellana.keynotedex.web.components

import kotlinext.js.js
import react.RBuilder
import react.dom.div

fun RBuilder.divWithHtml(classes: String? = null, html: dynamic) {
    div(classes) {
        attrs {
            val obj = js {
                this["__html"] = html
            }
            set("dangerouslySetInnerHTML", obj)
        }
    }
}
