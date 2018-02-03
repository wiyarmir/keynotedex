package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.model.Submission
import react.*
import react.dom.br
import react.dom.button
import react.dom.h2
import react.dom.p

class SubmissionsView : RComponent<SubmissionsProps, RState>() {
    override fun RBuilder.render() {
        h2 { +"Submissions" }
        props.submissions.apply {
            if (isEmpty()) {
                p { +"No submissions yet" }
                button { +"Create new" }
            } else {
                forEach {
                    submission {
                        attrs { submission = it }
                    }
                    br { }
                }
            }
        }
    }
}

external interface SubmissionsProps : RProps {
    var submissions: List<Submission>
}

fun RBuilder.submissions(handler: RHandler<SubmissionsProps>) = child(SubmissionsView::class, handler)
