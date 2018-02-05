package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.external.*
import es.guillermoorellana.keynotedex.web.model.*
import kotlinx.html.*
import react.*
import react.dom.*

class NavigationView : RComponent<NavigationProps, RState>() {
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
                if (props.getCurrentUser() == null) {
                    routeLink("/login") {
                        attrs { className = "btn btn-outline-success" }
                        +"Login"
                    }
                } else {
                    routeLink("/user/${props.getCurrentUser()!!.userId}") {
                        attrs { className = "nav-link" }
                        +props.getCurrentUser()!!.displayName
                    }
                }
            }
        }
    }
}

class NavigationProps(var getCurrentUser: () -> User?) : RProps

@ReactDsl
fun RBuilder.navigation(handler: RHandler<NavigationProps>) = child(NavigationView::class, handler)
