package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.comms.getSubmission
import es.guillermoorellana.keynotedex.web.components.editable.ChangeEvent
import es.guillermoorellana.keynotedex.web.components.editable.editableText
import es.guillermoorellana.keynotedex.web.components.editable.editableTextArea
import es.guillermoorellana.keynotedex.web.components.editable.get
import es.guillermoorellana.keynotedex.web.external.RouteResultProps
import es.guillermoorellana.keynotedex.web.loading
import es.guillermoorellana.keynotedex.web.model.Submission
import es.guillermoorellana.keynotedex.web.model.string
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.h3
import react.dom.hr
import react.dom.legend
import react.dom.p
import react.dom.style
import react.setState

private const val css = """

"""

class SubmissionScreen : RComponent<RouteResultProps<SubmissionRouteProps>, SubmissionViewState>() {

    override fun componentDidMount() {
        fetchSubmission()
    }

    override fun RBuilder.render() {
        style { +css }
        div("container") {
            loading(state.submission) { sub ->
                legend { +"This talk is ${sub.visibility.string()}." }
                button(classes = "btn btn-primary btn-lg") { +"Make not ${sub.visibility.string()}" }

                hr { }
                h3 {
                    editableText {
                        attrs {
                            value = sub.title
                            propName = "title"
                            change = { chg: ChangeEvent -> onChangeEvent(chg) }
                        }
                    }
                }
                editableTextArea {
                    attrs {
                        value = sub.abstract
                        propName = "abstract"
                        change = { chg: ChangeEvent -> onChangeEvent(chg) }
                    }
                }
                sub.type.let { if (it.isNotEmpty()) p { +"Type $it" } }
                sub.submittedTo.let { if (it.isNotEmpty()) p { +"Submitted to $it" } }
            }
        }
    }

    private fun onChangeEvent(chg: ChangeEvent) {
        setState {
            chg["abstract"]?.let { abstract ->
                submission = submission?.copy(abstract = abstract)
            }
            chg["title"]?.let { title ->
                submission = submission?.copy(title = title)
            }
        }
    }

    private fun fetchSubmission() {
        GlobalScope.promise {
            val submission = getSubmission(idFromRoute(props.match.params.submissionId))
            setState {
                this.submission = submission
            }
        }.catch {
            console.error(it)
        }
    }
}

private fun idFromRoute(submissionRoute: String) = submissionRoute.substringBefore('-')

external interface SubmissionRouteProps : RProps {
    val userId: String
    val submissionId: String
}

external interface SubmissionViewState : RState {
    var submission: Submission?
}
