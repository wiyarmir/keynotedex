package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.comms.logoutUser
import es.guillermoorellana.keynotedex.web.context.UserContext
import es.guillermoorellana.keynotedex.web.external.redirect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState

class SignOutScreen : RComponent<LogoutProps, RState>() {

    override fun componentDidMount() {
        GlobalScope.launch {
            logoutUser()
            props.nukeCurrentUser()
        }
    }

    override fun RBuilder.render() {
        UserContext.Consumer { user ->
            if (user == null) redirect(to = "/") { }
        }
    }
}

external interface LogoutProps : RProps {
    var nukeCurrentUser: () -> Unit
}

fun RBuilder.signOut(handler: RHandler<LogoutProps>) = child(SignOutScreen::class, handler)
