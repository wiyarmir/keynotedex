package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.dto.SubmissionVisibility
import es.guillermoorellana.keynotedex.requests.SubmissionCreateRequest
import es.guillermoorellana.keynotedex.web.comms.NetworkDataSource
import es.guillermoorellana.keynotedex.web.context.UserContext
import es.guillermoorellana.keynotedex.web.external.redirect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.form
import react.dom.input
import react.dom.label
import react.dom.style
import react.dom.textArea
import react.setState

// language=CSS
private const val css = """
.container{
    margin-top: 2em;
}

.input-title{
    font-size: 3rem;
}
"""

class AddSessionScreen : RComponent<RProps, AddSessionState>() {

    override fun AddSessionState.init() {
        title = ""
        abstract = ""
        isPrivate = false
        disabled = false
        redirectToProfile = false
    }

    override fun RBuilder.render() {
        style { +css }
        div("container") {
            form {
                attrs {
                    onSubmitFunction = { event ->
                        event.preventDefault()
                        submitNewSession()
                    }
                }
                div("form-group") {
                    input(classes = "form-control input-title", type = InputType.text) {
                        attrs {
                            id = "input-title"
                            disabled = state.disabled
                            required = true
                            placeholder = "Session title"
                            onChangeFunction = readInput { value -> title = value }
                        }
                    }
                }
                div("form-group") {
                    textArea(classes = "form-control") {
                        attrs {
                            id = "input-abstract"
                            disabled = state.disabled
                            required = true
                            placeholder = "What's your session about?"
                            onChangeFunction = readInput { value -> abstract = value }
                        }
                    }
                }
                div("form-check") {
                    input(classes = "form-check-input", type = InputType.checkBox) {
                        attrs {
                            id = "isVisible"
                            disabled = state.disabled
                            onChangeFunction = { event ->
                                val checked = event.target?.let { it as? HTMLInputElement }?.checked ?: false
                                setState {
                                    isPrivate = checked
                                }
                            }
                        }
                    }
                    label("form-check-label") {
                        attrs {
                            set("htmlFor", "isVisible")
                        }
                        +"Check this box to keep your submission private."
                    }
                }
                button(classes = "btn btn-primary", type = ButtonType.submit) {
                    +"Ship it!"
                }
            }
        }
        if (state.redirectToProfile) UserContext.Consumer { user ->
            redirect("/${user!!.userId}") {}
        }
    }

    private fun submitNewSession() {
        setState {
            disabled = true
        }
        val submissionCreateRequest = SubmissionCreateRequest(
            title = state.title,
            abstract = state.abstract,
            visibility = if (state.isPrivate) SubmissionVisibility.PRIVATE else SubmissionVisibility.PUBLIC
        )
        GlobalScope.launch {
            NetworkDataSource.postSubmission(submissionCreateRequest)
                .fold(
                    { error ->
                        setState {
                            // todo
                            disabled = false
                        }
                    },
                    {
                        setState {
                            redirectToProfile = true
                        }
                    }
                )
        }
    }

    private fun readInput(block: AddSessionState.(String) -> Unit): (Event) -> Unit = { event ->
        val value = event.inputValue
        setState { block(value) }
    }
}

external interface AddSessionState : RState {
    var title: String
    var abstract: String
    var isPrivate: Boolean

    var disabled: Boolean
    var redirectToProfile: Boolean
}

fun RBuilder.addSession() = child(AddSessionScreen::class) {}
