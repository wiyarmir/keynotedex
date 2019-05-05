package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.datasource.dto.SessionVisibility
import es.guillermoorellana.keynotedex.datasource.requests.SessionCreateRequest
import es.guillermoorellana.keynotedex.web.context.UserContext
import es.guillermoorellana.keynotedex.web.external.redirect
import es.guillermoorellana.keynotedex.web.repository.WithNetworkRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.FORM
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RState
import react.dom.RDOMBuilder
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

class AddSessionScreen : RComponent<AddSessionProps, AddSessionState>() {

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
                renderTitleInput()
                renderAbstractInput()
                renderVisibilityInput()
                button(classes = "btn btn-primary", type = ButtonType.submit) { +"Ship it!" }
            }
        }
        if (state.redirectToProfile) UserContext.Consumer { user ->
            redirect("/${user!!.userId}") {}
        }
    }

    private fun RDOMBuilder<FORM>.renderVisibilityInput() {
        div("form-check") {
            val inputId = "isVisible"
            label("form-check-label") {
                attrs { set("htmlFor", inputId) }
                +"Check this box to keep your session private."
            }
            input(classes = "form-check-input", type = InputType.checkBox) {
                attrs {
                    id = inputId
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val checked = event.target?.let { it as? HTMLInputElement }?.checked ?: false
                        setState { isPrivate = checked }
                    }
                }
            }
        }
    }

    private fun RDOMBuilder<FORM>.renderAbstractInput() {
        div("form-group") {
            val inputId = "input-abstract"
            label("form-check-label") {
                attrs { set("htmlFor", inputId) }
                +"Abstract"
            }
            textArea(classes = "form-control") {
                attrs {
                    id = inputId
                    disabled = state.disabled
                    required = true
                    placeholder = "What's your session about?"
                    onChangeFunction = readInput { value -> abstract = value }
                }
            }
        }
    }

    private fun RDOMBuilder<FORM>.renderTitleInput() {
        div("form-group") {
            val inputId = "input-title"
            label("form-check-label") {
                attrs { set("htmlFor", inputId) }
                +"Title"
            }
            input(classes = "form-control input-title", type = InputType.text) {
                attrs {
                    id = inputId
                    disabled = state.disabled
                    required = true
                    placeholder = "Session title"
                    onChangeFunction = readInput { value -> title = value }
                }
            }
        }
    }

    private fun submitNewSession() {
        setState { disabled = true }
        val sessionCreateRequest = SessionCreateRequest(
            title = state.title,
            abstract = state.abstract,
            visibility = if (state.isPrivate) SessionVisibility.PRIVATE else SessionVisibility.PUBLIC
        )
        GlobalScope.launch {
            props.networkRepository.postSession(sessionCreateRequest)
                .fold(
                    {
                        // todo
                        setState { disabled = false }
                    },
                    {
                        setState { redirectToProfile = true }
                    }
                )
        }
    }

    private fun readInput(block: AddSessionState.(String) -> Unit): (Event) -> Unit = { event ->
        val value = event.inputValue
        setState { block(value) }
    }
}

external interface AddSessionProps : WithNetworkRepository

external interface AddSessionState : RState {
    var title: String
    var abstract: String
    var isPrivate: Boolean

    var disabled: Boolean
    var redirectToProfile: Boolean
}

fun RBuilder.addSession(builder: RHandler<AddSessionProps>) = child(AddSessionScreen::class, builder)
