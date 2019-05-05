package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.NetworkDataSource
import es.guillermoorellana.keynotedex.datasource.SessionStorage
import es.guillermoorellana.keynotedex.repository.NetworkRepository
import kotlinext.js.require
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main() {
    val globalState = object {
        val sessionStorage = SessionStorage()
        val networkDataSource = NetworkRepository(
            NetworkDataSource(
                tokenProvider = {
                    ""
                }
            )
        )
    }

    require("narrow-jumbotron.css")
    require("main.css")

    window.onload = {
        render(document.getElementById("content")) {
            keynotedexApp {
                attrs {
                    networkRepository = globalState.networkDataSource
                    sessionStorage = globalState.sessionStorage
                }
            }
        }
    }
}
