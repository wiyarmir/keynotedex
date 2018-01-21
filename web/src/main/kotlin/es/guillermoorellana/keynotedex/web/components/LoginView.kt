package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.comms.*
import es.guillermoorellana.keynotedex.web.model.User
import kotlinx.coroutines.experimental.async
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

//language=CSS
const val css = """
.form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signin .form-signin-heading {
  margin-bottom: 10px;
  margin-top: 10px;
}
.form-signin .form-control {
  position: relative;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
"""

class LoginView : RComponent<LoginProps, LoginState>() {

    override fun LoginState.init() {
        disabled = false
    }

    override fun RBuilder.render() {
        div {
            style { +css }
            form(classes = "form-signin") {
                h2("form-signin-heading") { +"Please sign in" }
                label(classes = "sr-only") {
                    attrs {
                        htmlFor = "inputEmail"
                    }
                    +"Email address"
                }
                input(type = InputType.email, classes = "form-control") {
                    attrs {
                        id = "inputEmail"
                        placeholder = "Email"
                        required = true
                        autoFocus = true
                    }
                }
                label("sr-only") {
                    attrs {
                        htmlFor = "inputPassword"
                    }
                }
                input(type = InputType.password, classes = "form-control") {
                    attrs {
                        id = "inputPassword"
                        placeholder = "Password"
                        required = true
                    }
                }
                button(classes = "btn btn-lg btn-primary btn-block", type = ButtonType.submit) {
                    +"Sign in"
                    attrs {
                        onClickFunction = {
                            it.preventDefault()
                            doLogin()
                        }
                    }
                }
            }
        }
    }

    private fun doLogin() {
        setState {
            disabled = true
        }
        async {
            val user = login(state.login, state.password)
            loggedIn(user)
        }
            .catch { err -> loginFailed(err) }
    }

    private fun loggedIn(user: User) {
        props.userAssigned(user)
    }

    private fun loginFailed(err: Throwable) {
        if (err is LoginOrRegisterFailedException) {
            setState {
                disabled = false
//                errorMessage = err.message
            }
        } else {
            console.error("Login failed", err)
            setState {
                disabled = false
//                errorMessage = "Login failed: please reload page and try again"
            }
        }
    }
}

class LoginProps : RProps {
    var userAssigned: (User) -> Unit = {}
}

data class LoginState(
    var login: String,
    var password: String,
    var disabled: Boolean
) : RState
