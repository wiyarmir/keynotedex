package es.guillermoorellana.keynotedex.web

import kotlinext.js.*
import react.dom.*
import kotlin.browser.*

fun main(args: Array<String>) {
    require("narrow-jumbotron.css")

    window.onload = {
        render(document.getElementById("content")) {
            keynotedexApp {}
        }
    }
}
