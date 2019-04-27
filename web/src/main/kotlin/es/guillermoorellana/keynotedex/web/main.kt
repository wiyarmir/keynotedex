package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.NetworkDataSource
import es.guillermoorellana.keynotedex.web.repository.NetworkRepository
import es.guillermoorellana.keynotedex.web.repository.SessionStorage
import es.guillermoorellana.keynotedex.web.repository.sessionStorageTokenProvider
import kotlinext.js.require
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main() {
    val globalState = object {
        val sessionStorage = SessionStorage
        val networkDataSource = NetworkRepository(
            NetworkDataSource(
                tokenProvider = sessionStorageTokenProvider(sessionStorage)
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
