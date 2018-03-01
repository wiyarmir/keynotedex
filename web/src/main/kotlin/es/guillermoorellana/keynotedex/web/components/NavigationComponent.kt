package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.model.User
import kotlinx.html.ButtonType
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
                ul("nav navbar-nav mr-auto") {
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
                ul("nav navbar-nav navbar-right") {
                    if (props.getCurrentUser() == null) {
                        routeLink("/login") {
                            attrs { className = "btn btn-outline-success" }
                            +"Login"
                        }
                    } else {
                        li("dropdown") {
                            span("glyphicon glyphicon-user") { }
                            routeLink("/user/${props.getCurrentUser()!!.userId}") {
                                attrs { className = "dropdown-toggle" }
                                +props.getCurrentUser()!!.displayName
                            }
                        }
                    }
                }
            }
        }
    }
}

class NavigationProps(var getCurrentUser: () -> User?) : RProps

@ReactDsl
fun RBuilder.navigation(handler: RHandler<NavigationProps>) = child(NavigationView::class, handler)
