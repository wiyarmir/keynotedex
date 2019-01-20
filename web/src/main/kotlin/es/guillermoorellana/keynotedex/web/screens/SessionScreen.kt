package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.comms.NetworkDataSource
import es.guillermoorellana.keynotedex.web.components.editable.ChangeEvent
import es.guillermoorellana.keynotedex.web.components.editable.editableText
import es.guillermoorellana.keynotedex.web.components.editable.editableTextArea
import es.guillermoorellana.keynotedex.web.components.editable.get
import es.guillermoorellana.keynotedex.web.external.RouteResultProps
import es.guillermoorellana.keynotedex.web.loading
import es.guillermoorellana.keynotedex.web.model.Session
import es.guillermoorellana.keynotedex.web.model.flip
import es.guillermoorellana.keynotedex.web.model.string
import es.guillermoorellana.keynotedex.web.model.toDto
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.promise
import kotlinx.html.js.onClickFunction
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

// language=CSS
private const val css = """

"""

class SessionScreen : RComponent<RouteResultProps<SessionRouteProps>, SessionScreenState>() {

    override fun componentDidMount() {
        fetchSubmission()
    }

    override fun RBuilder.render() {
        style { +css }
        div("container") {
            loading(state.session) { sub ->
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

                hr { }

                legend { +"This talk is ${sub.visibility.string()}." }
                button(classes = "btn btn-primary btn-lg") {
                    +"Make not ${sub.visibility.string()}"
                    attrs {
                        onClickFunction = { onVisibilityChanged() }
                    }
                }
            }
        }
    }

    private fun onVisibilityChanged() {
        val session = state.session ?: return
        val updated = session.copy(visibility = session.visibility.flip())
        setState {
            this.session = updated
        }
        updateSession(updated)
    }

    private fun onChangeEvent(chg: ChangeEvent) {
        var session = state.session ?: return
        chg["abstract"]?.let { abstract ->
            session = session.copy(abstract = abstract)
        }
        chg["title"]?.let { title ->
            session = session.copy(title = title)
        }
        setState {
            this.session = session
        }
        if (session != state.session) updateSession(session)
    }

    private fun updateSession(session: Session) {
        GlobalScope.launch {
            NetworkDataSource.updateSubmission(session.toDto())
            fetchSubmission()
        }
    }

    private fun fetchSubmission() {
        GlobalScope.promise {
            val sessionId = cleanupSubmissionId(props.match.params.sessionId)
            val session = NetworkDataSource.getSubmission(sessionId)
            setState {
                this.session = session
            }
        }.catch {
            console.error(it)
        }
    }
}

private fun cleanupSubmissionId(id: String): String = id.split('-').last()

external interface SessionRouteProps : RProps {
    val userId: String
    val sessionId: String
}

external interface SessionScreenState : RState {
    var session: Session?
}
