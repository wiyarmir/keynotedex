package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.comms.updateUserProfile
import es.guillermoorellana.keynotedex.web.comms.userProfile
import es.guillermoorellana.keynotedex.web.components.profile.editableProfile
import es.guillermoorellana.keynotedex.web.components.profile.publicProfile
import es.guillermoorellana.keynotedex.web.external.RouteResultProps
import es.guillermoorellana.keynotedex.web.loading
import es.guillermoorellana.keynotedex.web.model.UserProfile
import kotlinx.coroutines.experimental.promise
import react.*
import react.dom.div
import react.dom.style

class UserScreen : RComponent<RouteResultProps<UserProps>, UserState>() {

    override fun UserState.init() {
        userProfile = null
    }


    override fun componentDidMount() {
        fetchUserProfileFromProps(props)
    }

    override fun componentWillReceiveProps(nextProps: RouteResultProps<UserProps>) {
        if (nextProps.match.params != props.match.params) {
            fetchUserProfileFromProps(nextProps)
        }
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
                        it.editable -> editableProfile {
                            attrs {
                                userProfile = it
                                onUserProfileUpdated = { postUserProfile(it) }
                            }
                        }
                        else -> publicProfile { attrs { userProfile = it } }
                    }
                }
            }
        }
    }

    private fun fetchUserProfileFromProps(props: RouteResultProps<UserProps>) {
        val userId = props.match.params.userId
        promise {
            val user = userProfile(userId)
            setState {
                this.userProfile = user
            }
        }.catch {
            console.error(it)
        }
    }

    private fun postUserProfile(userProfile: UserProfile) {
        promise {
            val updatedUserProfile = updateUserProfile(userProfile)
            setState {
                this.userProfile = updatedUserProfile
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
    var userProfile: UserProfile?
}
