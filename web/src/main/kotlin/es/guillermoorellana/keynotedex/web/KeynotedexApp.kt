package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.web.comms.checkSession
import es.guillermoorellana.keynotedex.web.components.navigation
import es.guillermoorellana.keynotedex.web.context.UserContext
import es.guillermoorellana.keynotedex.web.external.browserRouter
import es.guillermoorellana.keynotedex.web.external.route
import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.external.switch
import es.guillermoorellana.keynotedex.web.model.User
import es.guillermoorellana.keynotedex.web.screens.HomeScreen
import es.guillermoorellana.keynotedex.web.screens.NotFoundScreen
import es.guillermoorellana.keynotedex.web.screens.PrivacyPolicyScreen
import es.guillermoorellana.keynotedex.web.screens.SubmissionScreen
import es.guillermoorellana.keynotedex.web.screens.TOSScreen
import es.guillermoorellana.keynotedex.web.screens.UserScreen
import es.guillermoorellana.keynotedex.web.screens.signIn
import es.guillermoorellana.keynotedex.web.screens.signOut
import es.guillermoorellana.keynotedex.web.screens.signUp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.html.MAIN
import kotlinx.html.main
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.RDOMBuilder
import react.dom.a
import react.dom.div
import react.dom.footer
import react.dom.hr
import react.dom.p
import react.dom.style
import react.setState

class Application : RComponent<RProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        currentUser = null
        checkUserSession()
    }

    override fun RBuilder.render() {
        UserContext.Provider(state.currentUser) {
            browserRouter {
                div {
                    navigation { }
                    main("mt-0 mt-md-2 mt-lg-0") {
                        attrs { role = "main" }
                        navigationSwitch()
                        footer()
                    }
                }
            }
        }
    }

    private fun RDOMBuilder<MAIN>.navigationSwitch() {
        switch {
            route("/", HomeScreen::class, exact = true)
            route("/signout", exact = true) {
                signOut {
                    attrs {
                        nukeCurrentUser = { setState { currentUser = null } }
                    }
                }
            }
            route("/signin", exact = true) {
                signIn {
                    attrs {
                        onUserLoggedIn = { user -> setState { currentUser = user } }
                    }
                }
            }
            route("/signup", exact = true) {
                signUp {
                    attrs {
                        onUserLoggedIn = { user -> setState { currentUser = user } }
                    }
                }
            }
            route("/privacy", PrivacyPolicyScreen::class, exact = true)
            route("/terms", TOSScreen::class, exact = true)
            route("/events", NotFoundScreen::class, exact = true)
            route("/sessions", NotFoundScreen::class, exact = true)
            route("/:userId/:submissionId", SubmissionScreen::class, exact = true)
            route("/:userId", UserScreen::class, exact = true)
            route(NotFoundScreen::class)
        }
    }

    private fun RDOMBuilder<MAIN>.footer() {
        style {
            // language=CSS
            +"""
            footer {
                margin-top: 2em;
            }
            """.trimIndent()
        }
        footer("container") {
            hr { }
            p {
                +"Keynotedex, an "
                a(href = "https://github.com/wiyarmir/keynotedex", target = "blank") { +"OpenSource" }
                +" initiative by "
                a(href = "https://guillermoorellana.es", target = "blank") { +"Guillermo Orellana" }
                +" — "
                routeLink(to = "/terms") { +"Terms of Service" }
                +" — "
                routeLink(to = "/privacy") { +"Privacy Policy" }
            }
        }
    }

    private fun checkUserSession() {
        GlobalScope.promise {
            val user = checkSession()
            setState {
                currentUser = user
            }
        }.catch { throwable ->
            console.error(throwable)
            setState {
                currentUser = null
            }
        }
    }
}

class ApplicationPageState(var currentUser: User?) : RState

fun RBuilder.keynotedexApp(handler: RHandler<RProps>) = child(Application::class, handler)
