package es.guillermoorellana.keynotedex.presenter

import es.guillermoorellana.keynotedex.coroutines.CustomMainScope
import es.guillermoorellana.keynotedex.repository.model.Session
import es.guillermoorellana.keynotedex.repository.model.UserProfile
import es.guillermoorellana.keynotedex.usecase.GetUserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ProfilePresenter(
    private val view: ProfileView,
    private val getUserProfile: GetUserProfile,
    private val mainScope: CoroutineScope = CustomMainScope()
) : CoroutineScope by mainScope {

    init {
        loadSessions()
    }

    private lateinit var _state: ProfileViewState
    private var state: ProfileViewState
        get() = _state
        set(value) = when (value) {
            is ProfileViewState.Loading -> view.showLoading()
            is ProfileViewState.Content -> view.showProfile(value.profile)
            is ProfileViewState.Error -> view.showError("Error: ${value.throwable.message}")
        }

    fun loadSessions() {
        state = ProfileViewState.Loading
        launch {
            getUserProfile.execute("user1")
                .fold(
                    { throwable ->
                        println(throwable)
                        state = ProfileViewState.Error(throwable)
                    },
                    { profile -> state = ProfileViewState.Content(profile) }
                )
        }
    }

    fun onSessionClick(session: Session) {
        println("click! ${session.sessionId}")
    }

    fun destroy() = cancel()
}

interface ProfileView {
    fun showProfile(profile: UserProfile)
    fun showLoading()
    fun showError(error: String)
}

sealed class ProfileViewState {
    object Loading : ProfileViewState()
    data class Content(val profile: UserProfile) : ProfileViewState()
    data class Error(val throwable: Throwable) : ProfileViewState()
}
