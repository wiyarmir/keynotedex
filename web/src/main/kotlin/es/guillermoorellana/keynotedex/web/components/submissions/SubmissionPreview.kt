package es.guillermoorellana.keynotedex.web.components.submissions

import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.model.Submission
import react.*
import react.dom.div
import react.dom.h4
import react.dom.p

class SubmissionPreview : RComponent<SubmissionPreviewProps, RState>() {
    override fun RBuilder.render() {
        with(props.submission) {
            div("col-md-4") {
                h4 { +title }
                p { +abstract }
                p {
                    routeLink(to = "$userId/$submissionId") {
                        attrs {
                            className = "btn btn-secondary btn-lg"
                        }
                        +"View details"
                    }
                }
            }
        }
    }
}

external interface SubmissionPreviewProps : RProps {
    var submission: Submission
}

fun RBuilder.submissionPreview(handler: RHandler<SubmissionPreviewProps>) = child(SubmissionPreview::class, handler)
