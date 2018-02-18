package es.guillermoorellana.keynotedex.web.components.submissions

import es.guillermoorellana.keynotedex.web.model.*
import react.*
import react.dom.*

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
