package es.guillermoorellana.keynotedex.web.components

import kotlinx.html.ButtonType
import react.*
import react.dom.*

class NavigationView : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
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
                    li("nav-item") { a(href = "#/conferences", classes = "nav-link ") { +"Conferences" } }
                    li("nav-item") { a(href = "#/speakers", classes = "nav-link ") { +"Speakers" } }
                }
                a(href = "#/login", classes = "btn btn-outline-success") { +"Login" }
            }
        }
    }
}

fun RBuilder.navigation() = child(NavigationView::class) {}
