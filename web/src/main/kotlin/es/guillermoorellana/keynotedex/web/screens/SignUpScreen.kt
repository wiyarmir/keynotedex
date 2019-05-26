package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.datasource.responses.SignInResponse
import es.guillermoorellana.keynotedex.web.context.UserContext
import es.guillermoorellana.keynotedex.web.external.redirect
import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.repository.WithNetworkRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.FORM
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RState
import react.dom.RDOMBuilder
import react.dom.button
import react.dom.div
import react.dom.form
import react.dom.h2
import react.dom.input
import react.dom.label
import react.dom.style
import react.setState

// language=CSS
private const val css = """
.form-signup {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signup .form-signup-heading {
  margin-bottom: 10px;
  margin-top: 10px;
}
.form-signup .form-control {
  position: relative;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
  margin-bottom: 10px;
}
.form-signup .form-control:focus {
  z-index: 2;
}
"""

class SignUpScreen : RComponent<SignUpProps, SignUpState>() {
    override fun RBuilder.render() {
        style { +css }
        form(classes = "form-signup") {
            attrs {
                onSubmitFunction = { event ->
                    event.preventDefault()
                    doRegister()
                }
            }
            h2("form-signup-heading") { +"Welcome aboard!" }
            state.errorMessage?.let { message ->
                div("alert alert-danger") { +message }
            }
            renderNameInput()
            renderEmailInput()
            renderUserInput()
            renderPasswordInput()
            button(classes = "btn btn-lg btn-primary btn-block", type = ButtonType.submit) { +"Sign Up \uD83D\uDE80" }
            routeLink("/signin") {
                attrs { className = "btn btn-lg btn-secondary btn-block" }
                +"Sign In"
            }
        }
        UserContext.Consumer { user ->
            console.log(user)
            user?.let { redirect("/${it.userId}") {} }
        }
    }

    private fun RDOMBuilder<FORM>.renderNameInput() {
        div("form-group") {
            val inputId = "inputDisplayname"
            label(classes = "sr-only") {
                attrs { htmlFor = inputId }
                +"Display name"
            }
            input(type = InputType.text, classes = "form-control") {
                attrs {
                    id = inputId
                    placeholder = "Display name"
                    required = true
                    autoFocus = true
                    value = state.displayName
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val value = event.inputValue
                        setState { displayName = value }
                    }
                }
            }
        }
    }

    private fun RDOMBuilder<FORM>.renderEmailInput() {
        div("form-group") {
            val inputId = "inputEmail"
            label(classes = "sr-only") {
                attrs { htmlFor = inputId }
                +"Email address"
            }
            input(type = InputType.email, classes = "form-control") {
                attrs {
                    id = inputId
                    placeholder = "Email"
                    required = true
                    autoFocus = true
                    value = state.email
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val value = event.inputValue
                        setState { email = value }
                    }
                }
            }
        }
    }

    private fun RDOMBuilder<FORM>.renderUserInput() {
        div("form-group") {
            val inputId = "inputUsername"
            label(classes = "sr-only") {
                attrs { htmlFor = inputId }
                +"User name"
            }
            input(type = InputType.text, classes = "form-control") {
                attrs {
                    id = inputId
                    placeholder = "User name"
                    required = true
                    autoFocus = true
                    value = state.username
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val value = event.inputValue
                        setState { username = value }
                    }
                }
            }
        }
    }

    private fun RDOMBuilder<FORM>.renderPasswordInput() {
        div("form-group") {
            val inputId = "inputPassword"
            label("sr-only") {
                attrs { htmlFor = inputId }
                +"Password"
            }
            input(type = InputType.password, classes = "form-control") {
                attrs {
                    id = inputId
                    placeholder = "Password"
                    required = true
                    value = state.password
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val value = event.inputValue
                        setState { password = value }
                    }
                }
            }
        }
    }

    private fun doRegister() {
        setState { disabled = true }
        GlobalScope.launch {
            props.networkRepository.register(state.username, state.password, state.username, state.email)
                .fold(
                    { exception ->
                        setState {
                            disabled = false
                            errorMessage = exception.message.toString()
                        }
                    },
                    { response ->
                        props.onUserLoggedIn(response)
                    }
                )
        }
    }
}

external interface SignUpProps : WithNetworkRepository {
    var onUserLoggedIn: (SignInResponse) -> Unit
}

external interface SignUpState : RState {
    var displayName: String
    var email: String
    var username: String
    var password: String
    var disabled: Boolean
    var errorMessage: String?
}

fun RBuilder.signUp(handler: RHandler<SignUpProps>) = child(SignUpScreen::class, handler)
