package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.web.components.*
import react.dom.render
import kotlin.browser.*

fun main(args: Array<String>) {
    runtime.wrappers.require("narrow-jumbotron.css")

    window.onload = {
        render(document.getElementById("content")) {
            hashRouter {
                application()
            }
        }
    }
}
