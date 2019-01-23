package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.components.submissions.sessionPreview
import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.model.SessionPreview
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.h1
import react.dom.p

class HomeScreen : RComponent<RProps, RState>() {

    private val jumboHeading = "All about talks"
    private val lead = "Share here the talk you want to do, the talk you will do and the talk you have done."

    private val previews = listOf(preview(), preview(), preview())

    override fun RBuilder.render() {
        div {
            div("jumbotron") {
                div("container") {
                    h1("display-3") { +jumboHeading }
                    p("lead") { +lead }
                    p {
                        routeLink(to = "/signup") {
                            attrs {
                                className = "btn btn-primary btn-lg"
                            }
                            +"Sign Up"
                        }
                    }
                }
            }
            div("container") {
                div("row") {
                    previews.forEach {
                        sessionPreview {
                            attrs { preview = it }
                        }
                    }
                }
            }
        }
    }
}

private fun preview() = SessionPreview(
    userId = "user1",
    sessionId = "1",
    title = "Subheading",
    userDisplayName = "User 1"
)
