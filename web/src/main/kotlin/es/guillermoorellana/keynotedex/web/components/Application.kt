package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.external.*
import es.guillermoorellana.keynotedex.web.model.User
import react.*
import react.dom.*

class Application : RComponent<RProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        currentUser = null
    }

    override fun RBuilder.render() {
        hashRouter {
            div {
                navigation()
                switch {
                    route("/", HomeView::class, exact = true)
                    route("/login") {
                        login {
                            attrs {
                                onUserLoggedIn = { user -> userLoggedIn(user) }
                                isUserLoggedIn = { isCurrentUserLoggedIn() }
                            }
                        }
                    }
                    route("/conferences", ConferencesView::class)
                    route("/speakers", SpeakersView::class)
                }
                footer("container") {
                    p { +"© Keynotedex ${js("new Date().getFullYear()")}" }
                }
            }
        }
    }

    private fun isCurrentUserLoggedIn() = state.currentUser != null

    private fun userLoggedIn(user: User) {
        setState {
            currentUser = user
        }
    }
}

class ApplicationPageState(var currentUser: User?) : RState

fun RBuilder.application(handler: RHandler<RProps>) = child(Application::class, handler)
