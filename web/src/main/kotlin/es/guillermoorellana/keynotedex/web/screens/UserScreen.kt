package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.*
import es.guillermoorellana.keynotedex.web.comms.*
import es.guillermoorellana.keynotedex.web.components.profile.*
import es.guillermoorellana.keynotedex.web.external.*
import es.guillermoorellana.keynotedex.web.model.*
import kotlinx.coroutines.*
import react.*
import react.dom.*

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
        div("row justify-content-center") {
            div("col-10 col-offset-1 col-sm-9 col-xl-8") {
                loading(state.userProfile) {
                    editableProfile {
                        attrs {
                            editable = it.editable
                            userProfile = it
                            onUserProfileUpdated = { postUserProfile(it) }
                        }
                    }
                }
            }
        }
    }

    private fun fetchUserProfileFromProps(props: RouteResultProps<UserProps>) {
        val userId = props.match.params.userId
        GlobalScope.promise {
            val user = userProfile(userId)
            setState {
                this.userProfile = user
            }
        }.catch {
            console.error(it)
        }
    }

    private fun postUserProfile(userProfile: UserProfile) {
        GlobalScope.promise {
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
