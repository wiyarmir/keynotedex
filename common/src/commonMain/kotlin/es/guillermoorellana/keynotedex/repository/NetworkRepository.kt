package es.guillermoorellana.keynotedex.repository

import arrow.core.Try
import es.guillermoorellana.keynotedex.datasource.NetworkDataSource
import es.guillermoorellana.keynotedex.datasource.requests.SessionCreateRequest
import es.guillermoorellana.keynotedex.datasource.responses.SignInResponse
import es.guillermoorellana.keynotedex.repository.model.Conference
import es.guillermoorellana.keynotedex.repository.model.Session
import es.guillermoorellana.keynotedex.repository.model.UserProfile
import es.guillermoorellana.keynotedex.repository.model.toModel
import es.guillermoorellana.keynotedex.repository.model.toUpdateRequest
import es.guillermoorellana.keynotedex.datasource.dto.Session as DtoSession

class NetworkRepository(
    private val dataSource: NetworkDataSource
) {

    suspend fun register(userId: String, password: String, displayName: String, email: String): Try<SignInResponse> =
        dataSource.register(userId, password, displayName, email)

    suspend fun userProfile(userId: String): Try<UserProfile> =
        dataSource.userProfile(userId)
            .map { it.toModel() }

    suspend fun updateUserProfile(userProfile: UserProfile): Try<UserProfile> =
        dataSource.updateUserProfile(userProfile.toUpdateRequest())
            .map { it.toModel() }

    suspend fun login(userId: String, password: String): Try<SignInResponse> =
        dataSource.login(userId, password)

    suspend fun logoutUser(): Try<Unit> =
        dataSource.logoutUser()

    suspend fun getSession(submissionId: String): Try<Session> =
        dataSource.getSubmission(submissionId)
            .map { it.toModel() }

    suspend fun postSession(sessionCreateRequest: SessionCreateRequest): Try<Unit> =
        dataSource.postSubmission(sessionCreateRequest)

    suspend fun updateSession(session: DtoSession): Try<Unit> =
        dataSource.updateSubmission(session)

    suspend fun getEvents(): Try<List<Conference>> =
        dataSource.getEvents()
            .map { it.conferences }
            .map { it.map { it.toModel() } }
}
