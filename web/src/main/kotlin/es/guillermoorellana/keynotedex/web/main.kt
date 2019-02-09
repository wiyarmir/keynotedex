package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.web.comms.NetworkDataSource
import es.guillermoorellana.keynotedex.web.comms.NetworkService
import es.guillermoorellana.keynotedex.web.comms.SessionStorage
import es.guillermoorellana.keynotedex.web.comms.create
import kotlinext.js.require
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main() {
    val globalState = object {
        val sessionStorage = SessionStorage
        val networkDataSource = NetworkDataSource(
            NetworkService.create(sessionStorage)
        )
    }

    require("narrow-jumbotron.css")
    require("main.css")

    window.onload = {
        render(document.getElementById("content")) {
            keynotedexApp {
                attrs {
                    networkDataSource = globalState.networkDataSource
                    sessionStorage = globalState.sessionStorage
                }
            }
        }
    }
}
