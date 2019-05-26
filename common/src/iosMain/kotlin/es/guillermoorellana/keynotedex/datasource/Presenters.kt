package es.guillermoorellana.keynotedex.datasource

import es.guillermoorellana.keynotedex.presenter.ProfilePresenter
import es.guillermoorellana.keynotedex.presenter.ProfileView
import es.guillermoorellana.keynotedex.usecase.GetUserSessions

fun createProfilePresenter(view: ProfileView, getUserSessions: GetUserSessions) = ProfilePresenter(
    view = view,
    getUserSessions = getUserSessions,
    mainScope = CustomMainScope()
)
