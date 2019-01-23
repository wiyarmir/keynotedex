package es.guillermoorellana.keynotedex.web.screens

import arrow.core.Try
import es.guillermoorellana.keynotedex.web.comms.NetworkDataSource
import es.guillermoorellana.keynotedex.web.components.profile.editableProfile
import es.guillermoorellana.keynotedex.web.external.RouteResultProps
import es.guillermoorellana.keynotedex.web.loading
import es.guillermoorellana.keynotedex.web.model.UserProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
                loading(state.userProfile) { result ->
                    result.fold(
                        {
                            notFound()
                        },
                        { profile ->
                            editableProfile {
                                attrs {
                                    editable = profile.editable
                                    userProfile = profile
                                    onUserProfileUpdated = { postUserProfile(it) }
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun fetchUserProfileFromProps(props: RouteResultProps<UserProps>) {
        val userId = props.match.params.userId
        GlobalScope.launch {
            val user = NetworkDataSource.userProfile(userId)
            setState {
                this.userProfile = user
            }
        }
    }

    private fun postUserProfile(userProfile: UserProfile) {
        GlobalScope.launch {
            val updatedUserProfile = NetworkDataSource.updateUserProfile(userProfile)
            setState {
                this.userProfile = updatedUserProfile
            }
        }
    }
}

interface UserProps : RProps {
    var userId: String
}

interface UserState : RState {
    var userProfile: Try<UserProfile>?
}
