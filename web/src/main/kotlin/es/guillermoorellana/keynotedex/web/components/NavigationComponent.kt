package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.context.UserContext
import es.guillermoorellana.keynotedex.web.external.routeLink
import kotlinx.html.ButtonType
import kotlinx.html.id
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.ReactDsl
import react.dom.button
import react.dom.div
import react.dom.li
import react.dom.nav
import react.dom.span
import react.dom.ul

class NavBarComponent : RComponent<RProps, RState>() {
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
                        routeLink("/sessions") {
                            attrs { className = "nav-link" }
                            +"Sessions"
                        }
                    }
                    li("nav-item") {
                        routeLink("/events") {
                            attrs { className = "nav-link" }
                            +"Events"
                        }
                    }
                }
                div("nav navbar-nav navbar-right mt-2 mt-lg-0") {
                    UserContext.Consumer { user ->
                        when (user) {
                            null -> routeLink("/signin") {
                                attrs { className = "btn btn-outline-success" }
                                +"Login"
                            }
                            else -> routeLink("/${user.userId}") {
                                attrs { className = "btn btn-outline-light" }
                                +user.displayName
                            }
                        }
                    }
                }
            }
        }
    }
}

@ReactDsl
fun RBuilder.navigation(handler: RHandler<RProps>) = child(NavBarComponent::class, handler)
