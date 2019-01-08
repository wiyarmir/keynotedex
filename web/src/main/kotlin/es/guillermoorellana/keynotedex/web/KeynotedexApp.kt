package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.web.components.navigation
import es.guillermoorellana.keynotedex.web.external.browserRouter
import es.guillermoorellana.keynotedex.web.external.route
import es.guillermoorellana.keynotedex.web.external.switch
import es.guillermoorellana.keynotedex.web.model.User
import es.guillermoorellana.keynotedex.web.screens.ComingSoonScreen
import es.guillermoorellana.keynotedex.web.screens.HomeScreen
import es.guillermoorellana.keynotedex.web.screens.NotFoundScreen
import es.guillermoorellana.keynotedex.web.screens.RegisterScreen
import es.guillermoorellana.keynotedex.web.screens.SubmissionScreen
import es.guillermoorellana.keynotedex.web.screens.UserScreen
import es.guillermoorellana.keynotedex.web.screens.login
import es.guillermoorellana.keynotedex.web.screens.logout
import kotlinx.html.main
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.footer
import react.dom.p
import react.setState

class Application : RComponent<RProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        currentUser = null
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
                        route("/logout", exact = true) {
                            logout {
                                attrs {
                                    currentUser = state.currentUser
                                    nukeCurrentUser = { setState { currentUser = null } }
                                }
                            }
                        }
                        route("/login", exact = true) {
                            login {
                                attrs {
                                    currentUser = state.currentUser
                                    onUserLoggedIn = { user -> userLoggedIn(user) }
                                }
                            }
                        }
                        route("/conferences", ComingSoonScreen::class)
                        route("/register", RegisterScreen::class)
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
}

class ApplicationPageState(var currentUser: User?) : RState

fun RBuilder.keynotedexApp(handler: RHandler<RProps>) = child(Application::class, handler)
