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
    init {
        console.log("constructor")
    }

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
