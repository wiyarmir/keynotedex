package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.datasource.responses.SignInResponse
import es.guillermoorellana.keynotedex.web.context.UserContext
import es.guillermoorellana.keynotedex.web.external.redirect
import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.repository.WithNetworkRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RState
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
            label(classes = "sr-only") {
                attrs { htmlFor = "inputDisplayname" }
                +"Display name"
            }
            input(type = InputType.text, classes = "form-control") {
                attrs {
                    id = "inputDisplayname"
                    placeholder = "Display name"
                    required = true
                    autoFocus = true
                    value = state.displayName
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val value = event.inputValue
                        setState {
                            displayName = value
                        }
                    }
                }
            }
            label(classes = "sr-only") {
                attrs { htmlFor = "inputEmail" }
                +"Email address"
            }
            input(type = InputType.email, classes = "form-control") {
                attrs {
                    id = "inputEmail"
                    placeholder = "Email"
                    required = true
                    autoFocus = true
                    value = state.email
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val value = event.inputValue
                        setState {
                            email = value
                        }
                    }
                }
            }
            label(classes = "sr-only") {
                attrs { htmlFor = "inputUsername" }
                +"User name"
            }
            input(type = InputType.text, classes = "form-control") {
                attrs {
                    id = "inputUsername"
                    placeholder = "User name"
                    required = true
                    autoFocus = true
                    value = state.username
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val value = event.inputValue
                        setState {
                            username = value
                        }
                    }
                }
            }
            label("sr-only") {
                attrs { htmlFor = "inputPassword" }
            }
            input(type = InputType.password, classes = "form-control") {
                attrs {
                    id = "inputPassword"
                    placeholder = "Password"
                    required = true
                    value = state.password
                    disabled = state.disabled
                    onChangeFunction = { event ->
                        val value = event.inputValue
                        setState {
                            password = value
                        }
                    }
                }
            }
            button(classes = "btn btn-lg btn-primary btn-block", type = ButtonType.submit) { +"Sign Up \uD83D\uDE80" }
            routeLink("/signin") {
                attrs {
                    className = "btn btn-lg btn-secondary btn-block"
                }
                +"Sign In"
            }
        }
        UserContext.Consumer { user ->
            console.log(user)
            user?.let {
                redirect("/${it.userId}") {}
            }
        }
    }

    private fun doRegister() {
        setState {
            disabled = true
        }
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
