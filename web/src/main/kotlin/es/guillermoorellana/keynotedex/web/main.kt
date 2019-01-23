package es.guillermoorellana.keynotedex.web

import kotlinext.js.require
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    require("narrow-jumbotron.css")
    require("main.css")

    window.onload = {
        render(document.getElementById("content")) {
            keynotedexApp {}
        }
    }
}
