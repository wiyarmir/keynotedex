package es.guillermoorellana.keynotedex.presenter

import es.guillermoorellana.keynotedex.repository.model.Session
import es.guillermoorellana.keynotedex.usecase.GetUserSessions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ProfilePresenter(
    private val view: ProfileView,
    private val getUserSessions: GetUserSessions,
    private val mainScope: CoroutineScope = MainScope()
) : CoroutineScope by mainScope {

    init {
        loadSessions()
    }

    private lateinit var _state: MainViewState
    private var state: MainViewState
        get() = _state
        set(value) = when (value) {
            is MainViewState.Loading -> view.setText("Loading")
            is MainViewState.Content -> view.setText("Content: ${value.sessions}")
            is MainViewState.Error -> view.setText("Error: ${value.throwable.message}")
        }

    fun loadSessions() {
        state = MainViewState.Loading
        launch {
            getUserSessions.execute("user1")
                .fold(
                    { throwable ->
                        println(throwable)
                        state = MainViewState.Error(throwable)
                    },
                    { sessions -> state = MainViewState.Content(sessions) }
                )
        }
    }

    fun destroy() = cancel()
}

interface ProfileView {
    fun setText(text: String)
}

sealed class MainViewState {
    object Loading : MainViewState()
    data class Content(val sessions: List<Session>) : MainViewState()
    data class Error(val throwable: Throwable) : MainViewState()
}
