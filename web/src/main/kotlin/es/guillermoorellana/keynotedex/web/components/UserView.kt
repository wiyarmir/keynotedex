package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.*
import es.guillermoorellana.keynotedex.web.comms.*
import es.guillermoorellana.keynotedex.web.components.submissions.*
import es.guillermoorellana.keynotedex.web.external.*
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
        div("row justify-content-center") {
            div("col-12 col-md-9 col-xl-8") {
                loading(state.user) {
                    div("profile-container") {
                        h1 { +it.user.displayName }
                    }
                    submissions { attrs { submissions = it.submissions } }
                }
            }
        }
    }

    private fun fetchUser(userId: String) {
        promise {
            val user = userProfile(userId)
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
    var user: UserProfile?
}
