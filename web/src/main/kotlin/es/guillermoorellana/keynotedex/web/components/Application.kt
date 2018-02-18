package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.comms.*
import es.guillermoorellana.keynotedex.web.external.*
import es.guillermoorellana.keynotedex.web.model.*
import kotlinx.coroutines.experimental.*
import react.*
import react.dom.*

class Application : RComponent<RProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        currentUser = null
        checkUserSession()
    }

    override fun RBuilder.render() {
        browserRouter {
            div {
                navigation {
                    attrs {
                        getCurrentUser = { state.currentUser }
                    }
                }
                switch {
                    route("/", HomeView::class, exact = true)
                    route("/user/:userId", UserView::class, exact = true)
                    route("/login", exact = true) {
                        login {
                            attrs {
                                getCurrentUser = { state.currentUser }
                                onUserLoggedIn = { user -> userLoggedIn(user) }
                                isUserLoggedIn = { isCurrentUserLoggedIn() }
                            }
                        }
                    }
                    route("/conferences", ComingSoonView::class)
                    route("/speakers", ComingSoonView::class)
                    route("/submission/:submissionId", SubmissionView::class, exact = true)
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
        promise {
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
