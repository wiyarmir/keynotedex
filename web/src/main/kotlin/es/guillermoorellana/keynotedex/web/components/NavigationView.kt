package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.external.routeLink
import kotlinx.html.ButtonType
import react.*
import react.dom.*

class NavigationView : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        nav("navbar navbar-expand-md navbar-dark fixed-top bg-dark") {
            routeLink("/") {
                attrs { className = "navbar-brand" }
                +"Keynotedex"
            }
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
                    li("nav-item") {
                        routeLink("/conferences") {
                            attrs { className = "nav-link" }
                            +"Conferences"
                        }
                    }
                    li("nav-item") {
                        routeLink("/speakers") {
                            attrs { className = "nav-link" }
                            +"Speakers"
                        }
                    }
                }
                routeLink("/login") {
                    attrs { className = "btn btn-outline-success" }
                    +"Login"
                }
            }
        }
    }
}

@ReactDsl
fun RBuilder.navigation() = child(NavigationView::class) {}
