package es.guillermoorellana.keynotedex.web.components.submissions

import es.guillermoorellana.keynotedex.web.model.Submission
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.h2
import react.dom.hr
import react.dom.p

class Submissions : RComponent<SubmissionsProps, RState>() {
    override fun RBuilder.render() {
        h2 { +"Submissions" }
        hr { }
        div("row") {
            if (props.submissions.isEmpty()) {
                p { +"No submissions yet" }
            } else {
                props.submissions.forEach { sub ->
                    submissionCard {
                        attrs { submission = sub }
                    }
                }
            }
        }
    }
}

external interface SubmissionsProps : RProps {
    var submissions: List<Submission>
}

fun RBuilder.submissions(handler: RHandler<SubmissionsProps>) = child(Submissions::class, handler)
