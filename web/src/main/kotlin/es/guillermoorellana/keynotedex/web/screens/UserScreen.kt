package es.guillermoorellana.keynotedex.web.screens

import arrow.core.Try
import es.guillermoorellana.keynotedex.repository.model.UserProfile
import es.guillermoorellana.keynotedex.web.components.profile.editableProfile
import es.guillermoorellana.keynotedex.web.loading
import es.guillermoorellana.keynotedex.web.repository.WithNetworkRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RState
import react.dom.div
import react.setState

class UserScreen : RComponent<UserProps, UserState>() {

    override fun UserState.init() {
        userProfile = null
    }

    override fun componentDidMount() {
        fetchUserProfile(props.userId)
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

    private fun fetchUserProfile(userId: String) {
        GlobalScope.launch {
            val user = props.networkRepository.userProfile(userId)
            setState {
                this.userProfile = user
            }
        }
    }

    private fun postUserProfile(userProfile: UserProfile) {
        GlobalScope.launch {
            val updatedUserProfile = props.networkRepository.updateUserProfile(userProfile)
            setState {
                this.userProfile = updatedUserProfile
            }
        }
    }
}

interface UserProps : WithNetworkRepository {
    var userId: String
}

interface UserState : RState {
    var userProfile: Try<UserProfile>?
}

fun RBuilder.userScreen(builder: RHandler<UserProps>) = child(UserScreen::class, builder)
