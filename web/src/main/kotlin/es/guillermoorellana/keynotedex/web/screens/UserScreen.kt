package es.guillermoorellana.keynotedex.web.screens

import arrow.core.Try.Failure
import arrow.core.Try.Success
import es.guillermoorellana.keynotedex.web.comms.NetworkDataSource
import es.guillermoorellana.keynotedex.web.components.profile.editableProfile
import es.guillermoorellana.keynotedex.web.external.RouteResultProps
import es.guillermoorellana.keynotedex.web.loading
import es.guillermoorellana.keynotedex.web.model.UserProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.promise
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.setState

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
                loading(state.userProfile) { profile ->
                    editableProfile {
                        attrs {
                            editable = profile.editable
                            userProfile = profile
                            onUserProfileUpdated = { postUserProfile(it) }
                        }
                    }
                }
            }
        }
    }

    private fun fetchUserProfileFromProps(props: RouteResultProps<UserProps>) {
        val userId = props.match.params.userId
        GlobalScope.launch {
            val user = when (val req = NetworkDataSource.userProfile(userId)) {
                is Success -> req.value
                is Failure -> null
            }
            setState {
                this.userProfile = user
            }
        }
    }

    private fun postUserProfile(userProfile: UserProfile) {
        GlobalScope.promise {
            val updatedUserProfile = NetworkDataSource.updateUserProfile(userProfile)
            setState {
                this.userProfile = updatedUserProfile
            }
        }.catch { throwable ->
            console.error(throwable)
        }
    }
}

interface UserProps : RProps {
    var userId: String
}

interface UserState : RState {
    var userProfile: UserProfile?
}
