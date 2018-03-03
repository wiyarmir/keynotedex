package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.model.User
import kotlinx.html.ButtonType
import kotlinx.html.id
import react.*
import react.dom.*

class NavBarComponent : RComponent<NavigationProps, RState>() {
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
                            "data-target" to "#navbar",
                            "aria-controls" to "navbar",
                            "aria-expanded" to "false",
                            "aria-label" to "Toggle navigation"
                        )
                    )
                }
                span("navbar-toggler-icon") { }
            }
            div("collapse navbar-collapse") {
                attrs { id = "navbar" }
                ul("nav navbar-nav mr-auto mt-2 mt-lg-0") {
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
                div("nav navbar-nav navbar-right mt-2 mt-lg-0") {
                    val user = props.currentUser
                    when (user) {
                        null -> routeLink("/login") {
                            attrs { className = "btn btn-outline-success" }
                            +"Login"
                        }
                        else -> li("dropdown") {
                            routeLink("/user/${user.userId}") {
                                attrs { className = "dropdown-toggle" }
                                +user.displayName
                            }
                        }
                    }
                }
            }
        }
    }
}

external interface NavigationProps : RProps {
    var currentUser: User?
}

@ReactDsl
fun RBuilder.navigation(handler: RHandler<NavigationProps>) = child(NavBarComponent::class, handler)
