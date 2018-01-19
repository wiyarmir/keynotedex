package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.web.components.application
import kotlinext.js.*
import react.dom.render
import kotlin.browser.*

fun main(args: Array<String>) {
    require("narrow-jumbotron.css")

    window.onload = {
        render(document.getElementById("content")) {
            application()
        }
    }
}
