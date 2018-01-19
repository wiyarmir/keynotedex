package es.guillermoorellana.keynotedex.web

import kotlinx.html.*
import react.*
import react.dom.*
import kotlin.browser.*

fun main(args: Array<String>) {
    runtime.wrappers.require("narrow-jumbotron.css")

    window.onload = {
        render(document.getElementById("content")) {
            child(Application::class) {}
        }
    }
}

class Application : RComponent<ApplicationProps, ApplicationPageState>() {

    init {
        state = ApplicationPageState(MainView.Home)
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
                when (state.selected) {
                    MainView.Loading -> h1 { +"Loading..." }
                    MainView.Home -> child(HomeView::class) {}
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

class ApplicationProps : RProps
