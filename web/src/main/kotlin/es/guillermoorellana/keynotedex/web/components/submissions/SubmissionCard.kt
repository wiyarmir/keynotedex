package es.guillermoorellana.keynotedex.web.components.submissions

import es.guillermoorellana.keynotedex.web.external.routeLink
import react.*
import react.dom.div
import react.dom.h3
import react.dom.p

class SubmissionCard : RComponent<SubmissionProps, RState>() {
    override fun RBuilder.render() {
        with(props.submission) {
            div("col-6 col-sm-12 col-md-5 col-xl-4") {
                routeLink(to = "/submission/$submissionId") { h3 { +title } }
                abstract.let { if (it.isNotEmpty()) p { +it } }
                type.let { if (it.isNotEmpty()) p { +"Type $it" } }
                submittedTo.let { if (it.isNotEmpty()) p { +"Submitted to ${it}" } }
            }
        }
    }
}

external interface SubmissionProps : RProps {
    var submission: es.guillermoorellana.keynotedex.web.model.Submission
}

fun RBuilder.submissionCard(handler: RHandler<SubmissionProps>) = child(SubmissionCard::class, handler)
