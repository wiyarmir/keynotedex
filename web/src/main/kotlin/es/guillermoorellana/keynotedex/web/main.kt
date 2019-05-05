package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.datasource.NetworkDataSource
import es.guillermoorellana.keynotedex.datasource.SessionStorage
import es.guillermoorellana.keynotedex.repository.NetworkRepository
import kotlinext.js.require
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main() {
    val globalState = object {
        val sessionStorage = SessionStorage()
        val networkRepository = makeRepository(sessionStorage)
    }

    require("narrow-jumbotron.css")
    require("main.css")

    window.onload = {
        render(document.getElementById("content")) {
            keynotedexApp {
                attrs {
                    networkRepository = globalState.networkRepository
                    sessionStorage = globalState.sessionStorage
                }
            }
        }
    }
}

private fun makeRepository(sessionStorage: SessionStorage): NetworkRepository =
    NetworkRepository(
        dataSource = NetworkDataSource(
            sessionStorage = sessionStorage
        )
    )
