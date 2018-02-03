package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.model.Submission
import react.*
import react.dom.h2
import react.dom.p

class SubmissionView : RComponent<SubmissionProps, RState>() {
    override fun RBuilder.render() {
        h2 { +"Title" }
        p { +props.submission.title }
        h2 { +"Abstract" }
        p { props.submission.abstract }
        h2 { +"Type" }
        p { props.submission.type }
        h2 { +"Submitted to" }
        p { props.submission.submittedTo }
    }
}

external interface SubmissionProps : RProps {
    var submission: Submission
}

fun RBuilder.submission(handler: RHandler<SubmissionProps>) = child(SubmissionView::class, handler)
