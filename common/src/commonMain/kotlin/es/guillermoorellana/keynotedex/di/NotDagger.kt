package es.guillermoorellana.keynotedex.di

import es.guillermoorellana.keynotedex.datasource.NetworkDataSource
import es.guillermoorellana.keynotedex.datasource.SessionStorage
import es.guillermoorellana.keynotedex.presenter.ProfilePresenter
import es.guillermoorellana.keynotedex.presenter.ProfileView
import es.guillermoorellana.keynotedex.repository.NetworkRepository
import es.guillermoorellana.keynotedex.usecase.GetUserProfile

class NotDagger {

    val sessionStorage: SessionStorage = SessionStorage()

    private val dataSource = NetworkDataSource(sessionStorage = sessionStorage)
    val repository: NetworkRepository by lazy { NetworkRepository(dataSource = dataSource) }

    private fun getUserProfile() = GetUserProfile(repository)

    fun profilePresenter(view: ProfileView): ProfilePresenter = ProfilePresenter(view, getUserProfile())
}
