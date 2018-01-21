package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.comms.checkSession
import es.guillermoorellana.keynotedex.web.external.*
import es.guillermoorellana.keynotedex.web.model.User
import kotlinx.coroutines.experimental.async
import react.*
import react.dom.*

class Application : RComponent<RProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        currentUser = null
        checkUserSession()
    }

    override fun RBuilder.render() {
        hashRouter {
            div {
                navigation {
                    attrs {
                        getCurrentUser = { state.currentUser }
                    }
                }
                switch {
                    route("/", HomeView::class, exact = true)
                    route("/login") {
                        login {
                            attrs {
                                getCurrentUser = { state.currentUser }
                                onUserLoggedIn = { user -> userLoggedIn(user) }
                                isUserLoggedIn = { isCurrentUserLoggedIn() }
                            }
                        }
                    }
                    route("/conferences", ConferencesView::class)
                    route("/speakers", SpeakersView::class)
                    route("/user/:userId", UserView::class)
                    route(NotFoundView::class)
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

    private fun checkUserSession() {
        async {
            val user = checkSession()
            setState {
                currentUser = user
            }
        }.catch {
            setState {
                currentUser = null
            }
        }
    }
}

class ApplicationPageState(var currentUser: User?) : RState

fun RBuilder.application(handler: RHandler<RProps>) = child(Application::class, handler)
