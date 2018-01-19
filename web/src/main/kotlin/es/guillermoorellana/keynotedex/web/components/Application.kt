package es.guillermoorellana.keynotedex.web.components

import kotlinx.html.*
import react.*
import react.dom.*

class Application : RComponent<RProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        selected = MainView.Home
    }

    override fun RBuilder.render() {
        div {
            nav("navbar navbar-expand-md navbar-dark fixed-top bg-dark") {
                a(href = "#", classes = "navbar-brand") { +"Keynotedex" }
                button(type = ButtonType.button, classes = "navbar-toggler") {
                    attrs {
                        attributes.putAll(
                            arrayOf(
                                "data-toggle" to "collapse",
                                "data-target" to "#navbarsExampleDefault",
                                "aria-controls" to "navbarsExampleDefault",
                                "aria-expanded" to "false",
                                "aria-label" to "Toggle navigation"
                            )
                        )
                    }
                    span("navbar-toggler-icon") { }
                }
                div("collapse navbar-collapse") {
                    ul("navbar-nav mr-auto") {
                        li("nav-item") { a(href = "#", classes = "nav-link active") { +"Home" } }
                        li("nav-item") { a(href = "#", classes = "nav-link ") { +"Conferences" } }
                        li("nav-item") { a(href = "#", classes = "nav-link ") { +"Speakers" } }
                    }
                    a(href = "#", classes = "btn btn-outline-success") { +"Login" }
                }
            }
            main {
                attrs { role = "main" }
                loading(state.selected) { homeView() }
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
