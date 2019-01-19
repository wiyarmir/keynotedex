package es.guillermoorellana.keynotedex.web.components.submissions

import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.model.Submission
import es.guillermoorellana.keynotedex.web.model.SubmissionVisibility.PRIVATE
import es.guillermoorellana.keynotedex.web.model.SubmissionVisibility.PUBLIC
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.h3
import react.dom.p

class SubmissionCard : RComponent<SubmissionProps, RState>() {
    override fun RBuilder.render() {
        with(props.submission) {
            div("col-12 col-sm-6 col-xl-4") {
                routeLink(to = "/$userId/$submissionSlug-$submissionId") { h3 { +title() } }
                abstract.let { if (it.isNotEmpty()) p { +it } }
                type.let { if (it.isNotEmpty()) p { +"Type $it" } }
                submittedTo.let { if (it.isNotEmpty()) p { +"Submitted to $it" } }
            }
        }
    }
}

private fun Submission.title() = when (visibility) {
    PUBLIC -> title
    PRIVATE -> "\uD83D\uDD12 $title"
}

private val Submission.submissionSlug: String
    get() = title
        .replace(' ', '-')
        .toLowerCase()
        .replace(Regex("((?!([a-z0-9\\-])).)"), "")

external interface SubmissionProps : RProps {
    var submission: Submission
}

fun RBuilder.submissionCard(handler: RHandler<SubmissionProps>) = child(SubmissionCard::class, handler)
