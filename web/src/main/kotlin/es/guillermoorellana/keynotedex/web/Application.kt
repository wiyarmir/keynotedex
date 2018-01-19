package es.guillermoorellana.keynotedex.web

import kotlinx.html.ButtonType
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.h1
import kotlinx.html.h4
import kotlinx.html.li
import kotlinx.html.main
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.ul
import react.ReactComponentNoProps
import react.ReactComponentNoState
import react.ReactComponentSpec
import react.dom.ReactDOM
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    runtime.wrappers.require("narrow-jumbotron.css")

    window.onload = {
        ReactDOM.render(document.getElementById("content")) {
            div {
                Application {}
            }
        }
    }
}

class Application : ReactDOMComponent<ReactComponentNoProps, ReactComponentNoState>() {
    companion object : ReactComponentSpec<Application, ReactComponentNoProps, ReactComponentNoState>

    private val jumboHeading = "Jumbo heading"
    private val lead =
            "Cras justo odio, dapibus ac facilisis in, egestas eget quam. " +
                    "Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, " +
                    "ut fermentum massa justo sit amet risus."

    init {
        state = ReactComponentNoState()
    }

    override fun ReactDOMBuilder.render() {
        div("container") {
            nav("navbar navbar-expand-md navbar-dark fixed-top bg-dark") {
                a(href = "#", classes = "navbar-brand") { +"Keynotedex" }
                button(type = ButtonType.button, classes = "navbar-toggler") {
                    attributes.putAll(arrayOf(
                            "data-toggle" to "collapse",
                            "data-target" to "#navbarsExampleDefault",
                            "aria-controls" to "navbarsExampleDefault",
                            "aria-expanded" to "false",
                            "aria-label" to "Toggle navigation"
                    ))
                    span("navbar-toggler-icon") { }
                }
                div("collapse navbar-collapse") {
                    ul("navbar-nav mr-auto") {
                        li("nav-item") { a(href = "#", classes = "nav-link active") { +"Home" } }
                        li("nav-item") { a(href = "#", classes = "nav-link ") { +"Conferences" } }
                        li("nav-item") { a(href = "#", classes = "nav-link ") { +"Speakers" } }
                        li("nav-item") { a(href = "#", classes = "nav-link ") { +"Login" } }
                    }
                }
            }
            main {
                role = "main"
                div("jumbotron") {
                    h1("display-3") { +jumboHeading }
                    p("lead") { +lead }
                    p { a(href = "#", classes = "btn btn-lg btn-success") { +"Sign up" } }
                }
                div("row marketing") {
                    div("col-lg-6") {
                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }

                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }

                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }
                    }
                    div("col-lg-6") {
                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }

                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }

                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }
                    }
                }
            }
            footer("footer") {
                p { +"© Keynotedex ${js("new Date().getFullYear()")}" }
            }
        }
    }
}
