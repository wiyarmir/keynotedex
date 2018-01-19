package es.guillermoorellana.keynotedex.web.components

import kotlinx.html.main
import react.*
import react.dom.*

class Application : RComponent<RProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        selected = MainView.Home
    }

    override fun RBuilder.render() {
        div {
            navigation()
            main {
                attrs { role = "main" }
                hashRouter {
                    switch {
                        route("/", HomeView::class, exact = true)
                        route("/login", LoginView::class)
                        route("/conferences", ConferencesView::class)
                        route("/speakers", SpeakersView::class)
                    }
                }
            }
            footer("container") {
                p { +"© Keynotedex ${js("new Date().getFullYear()")}" }
            }
        }
    }
}

enum class MainView {
    Loading,
    Home
}

class ApplicationPageState(
    var selected: MainView
) : RState

fun RBuilder.application() = child(Application::class) {}
