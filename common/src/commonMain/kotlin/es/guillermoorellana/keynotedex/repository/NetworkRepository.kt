package es.guillermoorellana.keynotedex.repository

import arrow.core.Try
import es.guillermoorellana.keynotedex.datasource.NetworkDataSource
import es.guillermoorellana.keynotedex.datasource.dto.Submission
import es.guillermoorellana.keynotedex.datasource.requests.SubmissionCreateRequest
import es.guillermoorellana.keynotedex.datasource.responses.SignInResponse
import es.guillermoorellana.keynotedex.repository.model.UserProfile
import es.guillermoorellana.keynotedex.repository.model.toModel
import es.guillermoorellana.keynotedex.repository.model.toUpdateRequest

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

    suspend fun logoutUser() =
        dataSource.logoutUser()

    suspend fun getSubmission(submissionId: String) =
        dataSource.getSubmission(submissionId)
            .map { it.toModel() }

    suspend fun postSubmission(submissionCreateRequest: SubmissionCreateRequest) =
        dataSource.postSubmission(submissionCreateRequest)

    suspend fun updateSubmission(submission: Submission) =
        dataSource.updateSubmission(submission)
}
