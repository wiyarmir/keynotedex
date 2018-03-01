package es.guillermoorellana.keynotedex.web.components.submissions

import es.guillermoorellana.keynotedex.web.model.Submission
import react.*
import react.dom.div
import react.dom.h2
import react.dom.hr
import react.dom.p

class Submissions : RComponent<SubmissionsProps, RState>() {
    override fun RBuilder.render() {
        h2 { +"Submissions" }
        hr { }
        div("row") {
            props.submissions
                .apply {
                    if (isEmpty()) {
                        p { +"No submissions yet" }
//                Only when logged in??
//                button { +"Create new" }
                    } else {
                        forEach {
                            submissionCard {
                                attrs { submission = it }
                            }
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
