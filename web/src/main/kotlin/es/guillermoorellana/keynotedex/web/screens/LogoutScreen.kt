package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.comms.logoutUser
import es.guillermoorellana.keynotedex.web.external.redirect
import es.guillermoorellana.keynotedex.web.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState

class LogoutScreen : RComponent<LogoutProps, RState>() {

    override fun componentDidMount() {
        GlobalScope.launch {
            logoutUser()
            props.nukeCurrentUser()
        }
    }

    override fun RBuilder.render() {
        if (props.currentUser == null) redirect(to = "/") { }
    }
}

external interface LogoutProps : RProps {
    var currentUser: User?
    var nukeCurrentUser: () -> Unit
}

fun RBuilder.logout(handler: RHandler<LogoutProps>) = child(LogoutScreen::class, handler)
