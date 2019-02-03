package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.responses.LoginResponse
import es.guillermoorellana.keynotedex.web.comms.NetworkDataSource
import es.guillermoorellana.keynotedex.web.comms.SessionStorage
import es.guillermoorellana.keynotedex.web.components.navigation
import es.guillermoorellana.keynotedex.web.context.UserContext
import es.guillermoorellana.keynotedex.web.external.browserRouter
import es.guillermoorellana.keynotedex.web.external.getClaim
import es.guillermoorellana.keynotedex.web.external.jwtDecode
import es.guillermoorellana.keynotedex.web.external.route
import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.external.switch
import es.guillermoorellana.keynotedex.web.model.User
import es.guillermoorellana.keynotedex.web.screens.HomeScreen
import es.guillermoorellana.keynotedex.web.screens.NotFoundScreen
import es.guillermoorellana.keynotedex.web.screens.PrivacyPolicyScreen
import es.guillermoorellana.keynotedex.web.screens.SessionRouteProps
import es.guillermoorellana.keynotedex.web.screens.TOSScreen
import es.guillermoorellana.keynotedex.web.screens.UserProps
import es.guillermoorellana.keynotedex.web.screens.addSession
import es.guillermoorellana.keynotedex.web.screens.sessionScreen
import es.guillermoorellana.keynotedex.web.screens.signIn
import es.guillermoorellana.keynotedex.web.screens.signOut
import es.guillermoorellana.keynotedex.web.screens.signUp
import es.guillermoorellana.keynotedex.web.screens.userScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.main
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.a
import react.dom.div
import react.dom.footer
import react.dom.hr
import react.dom.p
import react.dom.style
import react.setState

class Application : RComponent<ApplicationProps, ApplicationPageState>() {

    override fun ApplicationPageState.init() {
        currentUser = null
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

    private fun RBuilder.navigationSwitch() {
        switch {
            route("/", HomeScreen::class, exact = true)
            route("/signout", exact = true) {
                signOut {
                    attrs {
                        networkDataSource = props.networkDataSource
                        nukeCurrentUser = { setState { currentUser = null } }
                    }
                }
            }
            route("/signin", exact = true) {
                signIn {
                    attrs {
                        networkDataSource = props.networkDataSource
                        onUserLoggedIn = ::onUserLoggedIn
                    }
                }
            }
            route("/signup", exact = true) {
                signUp {
                    attrs {
                        networkDataSource = props.networkDataSource
                        onUserLoggedIn = ::onUserLoggedIn
                    }
                }
            }
            route("/privacy", PrivacyPolicyScreen::class, exact = true)
            route("/terms", TOSScreen::class, exact = true)
            route("/events", NotFoundScreen::class, exact = true)
            route("/sessions", NotFoundScreen::class, exact = true)
            route("/sessions/add", exact = true) {
                addSession {
                    attrs {
                        networkDataSource = props.networkDataSource
                    }
                }
            }
            route<SessionRouteProps>("/:userId/:sessionId", exact = true) { routeProps ->
                sessionScreen {
                    attrs {
                        userId = routeProps.match.params.userId
                        sessionId = routeProps.match.params.sessionId
                        networkDataSource = props.networkDataSource
                    }
                }
            }
            route<UserProps>("/:userId", exact = true) { routeProps ->
                userScreen {
                    attrs {
                        userId = routeProps.match.params.userId
                        networkDataSource = props.networkDataSource
                    }
                }
            }
            route(NotFoundScreen::class)
        }
    }

    private fun RBuilder.footer() {
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

    private fun onUserLoggedIn(response: LoginResponse) {
        props.sessionStorage.put(response.jwtToken)
        val decoded = jwtDecode(response.jwtToken)
        val userId = decoded.getClaim("id")
        GlobalScope.launch {
            props.networkDataSource.userProfile(userId)
                .map { it.user }
                .fold(
                    { console.error(it) },
                    { user ->
                        setState {
                            currentUser = user
                        }
                    })
        }
    }
}

external interface ApplicationProps : RProps {
    var networkDataSource: NetworkDataSource
    var sessionStorage: SessionStorage
}

external interface ApplicationPageState : RState {
    var currentUser: User?
}

fun RBuilder.keynotedexApp(handler: RHandler<ApplicationProps>) = child(Application::class, handler)
