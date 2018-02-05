package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.comms.*
import es.guillermoorellana.keynotedex.web.components.submissions.*
import es.guillermoorellana.keynotedex.web.external.*
import es.guillermoorellana.keynotedex.web.loading
import es.guillermoorellana.keynotedex.web.model.*
import kotlinx.coroutines.experimental.*
import react.*
import react.dom.*

class UserView : RComponent<RouteResultProps<UserProps>, UserState>() {

    override fun UserState.init() {
    }

    override fun componentDidMount() {
        fetchUser(props.match.params.userId)
    }

    override fun RBuilder.render() {
        style {
            //language=CSS
            +".profile-container { padding: 40px 15px; text-align: center; }"
        }
        div("row") {
            div("col-md-10 offset-md-1") {
                loading(state.user) {
                    div("profile-container") {
                        h1 { +it.displayName }
                    }
                    submissions { attrs { submissions = it.submissions } }
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
            console.error(it)
        }
    }
}

interface UserProps : RProps {
    var userId: String
}

interface UserState : RState {
    var user: User?
}