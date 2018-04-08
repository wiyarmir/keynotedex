package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.web.comms.checkSession
import es.guillermoorellana.keynotedex.web.components.navigation
import es.guillermoorellana.keynotedex.web.external.browserRouter
import es.guillermoorellana.keynotedex.web.external.route
import es.guillermoorellana.keynotedex.web.external.switch
import es.guillermoorellana.keynotedex.web.model.User
import es.guillermoorellana.keynotedex.web.screens.*
import kotlinx.coroutines.experimental.promise
import kotlinx.html.main
import react.*
import react.dom.div
import react.dom.footer
import react.dom.p

class Application : RComponent<RProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        currentUser = null
        checkUserSession()
    }

    override fun RBuilder.render() {
        browserRouter {
            div {
                navigation {
                    attrs { currentUser = state.currentUser }
                }
                main("mt-0 mt-md-2 mt-lg-0") {
                    attrs { role = "main" }
                    switch {
                        route("/", HomeScreen::class, exact = true)
                        route("/login", exact = true) {
                            login {
                                attrs {
                                    currentUser = state.currentUser
                                    onUserLoggedIn = { user -> userLoggedIn(user) }
                                }
                            }
                        }
                        route("/conferences", ComingSoonScreen::class)
                        route("/speakers", ComingSoonScreen::class)
                        route("/:userId/:submissionId", SubmissionScreen::class, exact = true)
                        route("/:userId", UserScreen::class, exact = true)
                        route(NotFoundScreen::class)
                    }
                    footer("container") {
                        p { +"© Keynotedex ${js("new Date().getFullYear()")}" }
                    }
                }
            }
        }
    }

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
            console.error(it)
            setState {
                currentUser = null
            }
        }
    }
}

class ApplicationPageState(var currentUser: User?) : RState

fun RBuilder.keynotedexApp(handler: RHandler<RProps>) = child(Application::class, handler)
