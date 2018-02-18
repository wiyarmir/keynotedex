package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.*
import es.guillermoorellana.keynotedex.web.comms.*
import es.guillermoorellana.keynotedex.web.components.profile.*
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
                loading(state.userProfile) {
                    when {
                        it.editable -> editableProfile { attrs { userProfile = it } }
                        else -> publicProfile { attrs { userProfile = it } }
                    }
                }
            }
        }
    }

    private fun fetchUser(userId: String) {
        promise {
            val user = userProfile(userId)
            setState {
                this.userProfile = user
            }
        }.catch {
            console.error(it)
            setState {
                // not found?
            }
        }
    }
}

interface UserProps : RProps {
    var userId: String
}

interface UserState : RState {
    var userProfile: UserProfile?
}
