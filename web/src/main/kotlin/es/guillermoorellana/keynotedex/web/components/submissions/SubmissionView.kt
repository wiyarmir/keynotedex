package es.guillermoorellana.keynotedex.web.components.submissions

import es.guillermoorellana.keynotedex.web.model.*
import react.*
import react.dom.*

class SubmissionView : RComponent<SubmissionProps, RState>() {
    override fun RBuilder.render() {
        div("col-6 col-sm-12 col-md-5 col-xl-4") {
            h3 { +props.submission.title }
            props.submission.abstract.let { if (it.isNotEmpty()) p { +it } }
            props.submission.type.let { if (it.isNotEmpty()) p { +"Type $it" } }
            props.submission.submittedTo.let { if (it.isNotEmpty()) p { +"Submitted to ${it}" } }
        }
    }
}

external interface SubmissionProps : RProps {
    var submission: Submission
}

fun RBuilder.submission(handler: RHandler<SubmissionProps>) = child(
    SubmissionView::class, handler
)
