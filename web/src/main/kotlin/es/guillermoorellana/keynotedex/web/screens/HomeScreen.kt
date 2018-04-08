package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.components.submissions.submissionPreview
import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.model.Submission
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.h1
import react.dom.p

class HomeScreen : RComponent<RProps, RState>() {

    private val jumboHeading = "All about talks"
    private val lead = "Share here the talk you will give, the talk you want to give and the talk you have given."

    override fun RBuilder.render() {
        div {
            div("jumbotron") {
                div("container") {

                    h1("display-3") { +jumboHeading }
                    p("lead") { +lead }
                    p {
                        routeLink(to = "register") {
                            attrs {
                                className = "btn btn-primary btn-lg"
                            }
                            +"Join"
                        }
                    }
                }
            }
            div("container") {
                div("row") {
                    submissionPreview {
                        attrs { submission = submission() }
                    }
                    submissionPreview {
                        attrs { submission = submission() }
                    }
                    submissionPreview {
                        attrs { submission = submission() }
                    }
                }
            }
        }
    }
}

private fun submission() = Submission(
    userId = "user1",
    submissionId = "1",
    title = "Subheading",
    abstract = "Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum.",
    type = "",
    submittedTo = ""
)
