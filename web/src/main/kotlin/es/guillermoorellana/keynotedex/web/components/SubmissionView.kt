package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.*
import es.guillermoorellana.keynotedex.web.comms.*
import es.guillermoorellana.keynotedex.web.external.*
import es.guillermoorellana.keynotedex.web.model.*
import kotlinx.coroutines.experimental.*
import react.*
import react.dom.*

class SubmissionView : RComponent<RouteResultProps<SubmissionRouteProps>, SubmissionViewState>() {

    override fun componentDidMount() {
        fetchSubmission()
    }

    override fun RBuilder.render() {
        div("container") {
            loading(state.submission) {
                with(it) {
                    h3 { +title }
                    abstract.let { if (it.isNotEmpty()) p { +it } }
                    type.let { if (it.isNotEmpty()) p { +"Type $it" } }
                    submittedTo.let { if (it.isNotEmpty()) p { +"Submitted to $it" } }
                }
            }
        }
    }

    private fun idFromRoute(submissionRoute: String) = submissionRoute.substringBefore('-')

    private fun fetchSubmission() {
        promise {
            val submission = getSubmission(idFromRoute(props.match.params.submissionId))
            setState {
                this.submission = submission
            }
        }.catch {
            console.error(it)
        }
    }

}

external interface SubmissionRouteProps : RProps {
    val submissionId: String
}

external interface SubmissionViewState : RState {
    var submission: Submission?
}
