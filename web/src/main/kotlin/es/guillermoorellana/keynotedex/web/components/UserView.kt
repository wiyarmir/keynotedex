package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.comms.user
import es.guillermoorellana.keynotedex.web.external.RouteResultProps
import es.guillermoorellana.keynotedex.web.model.User
import kotlinx.coroutines.experimental.async
import react.*
import react.dom.*

class UserView : RComponent<RouteResultProps<UserProps>, UserState>() {

    override fun UserState.init() {

    }

    override fun componentWillReceiveProps(nextProps: RouteResultProps<UserProps>) {
        fetchUser(nextProps.match.params.userId)
    }

    override fun RBuilder.render() {
        style {
            //language=CSS
            +".profile-container { padding: 40px 15px; text-align: center; }"
        }
        div("row") {
            div("col-md-12") {
                div("profile-container") {
                    h1 { +"${state.user?.displayName}" }
                }
            }
        }
    }

    private fun fetchUser(userId: String) {
        async {
            val user = user(userId)
            setState {
                this.user = user
            }
        }.catch {

        }
    }
}

interface UserProps : RProps {
    var userId: String
}

interface UserState : RState {
    var user: User?
}
