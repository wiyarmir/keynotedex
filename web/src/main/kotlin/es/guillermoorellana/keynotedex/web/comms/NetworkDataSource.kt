package es.guillermoorellana.keynotedex.web.comms

import arrow.core.Try
import arrow.core.Try.Failure
import arrow.core.orNull
import es.guillermoorellana.keynotedex.api.Api
import es.guillermoorellana.keynotedex.dto.Submission
import es.guillermoorellana.keynotedex.dto.User
import es.guillermoorellana.keynotedex.requests.SubmissionCreateRequest
import es.guillermoorellana.keynotedex.requests.SubmissionUpdateRequest
import es.guillermoorellana.keynotedex.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import es.guillermoorellana.keynotedex.responses.SubmissionResponse
import es.guillermoorellana.keynotedex.responses.UserProfileResponse
import es.guillermoorellana.keynotedex.responses.UserResponse
import es.guillermoorellana.keynotedex.web.model.UserProfile
import es.guillermoorellana.keynotedex.web.model.toModel
import es.guillermoorellana.keynotedex.web.model.toUpdateRequest
import kotlinx.coroutines.await
import org.w3c.dom.url.URLSearchParams
import org.w3c.fetch.Response
import react.RProps
import kotlinx.serialization.json.JSON as KJSON

class NetworkDataSource(private val networkService: NetworkService) {

    suspend fun register(userId: String, password: String, displayName: String, email: String) =
        networkService.post(
            Api.V1.Paths.register,
            URLSearchParams().apply {
                append("userId", userId)
                append("password", password)
                append("displayName", displayName)
                append("email", email)
            }
        )
            .flatMap { parseUserProfileResponse(it) }
            .map { it.user.toModel() }

    suspend fun userProfile(userId: String): Try<UserProfile> =
        networkService.get(Api.V1.Paths.user.replace("{userId}", userId), null)
            .flatMap { parseUserProfileResponse(it) }
            .map { it.toModel() }

    suspend fun updateUserProfile(userProfile: UserProfile): Try<UserProfile> {
        val userId = userProfile.user.userId
        val body = KJSON.stringify(UserProfileUpdateRequest.serializer(), userProfile.toUpdateRequest())
        return networkService.put(Api.V1.Paths.user.replace("{userId}", userId), body)
            .flatMap { parseUserProfileResponse(it) }
            .map { it.toModel() }
    }

    suspend fun login(userId: String, password: String): es.guillermoorellana.keynotedex.web.model.User? {
        val body = URLSearchParams().apply {
            append("userId", userId)
            append("password", password)
        }
        return networkService.post(Api.V1.Paths.login, body)
            .flatMap { parseUserResponse(it) }
            .map { it.toModel() }
            .orNull()
    }

    suspend fun logoutUser() = networkService.post(Api.V1.Paths.logout, null)

    suspend fun getSubmission(submissionId: String) =
        networkService.get(Api.V1.Paths.submissions.replace("{submissionId?}", submissionId), null)
            .flatMap { parseSubmissionResponse(it) }
            .map { it.toModel() }

    suspend fun postSubmission(submissionCreateRequest: SubmissionCreateRequest) =
        networkService.post(
            Api.V1.Paths.submissions.replace("{submissionId?}", ""),
            KJSON.stringify(SubmissionCreateRequest.serializer(), submissionCreateRequest)
        )

    suspend fun updateSubmission(submission: Submission) =
        networkService.put(
            Api.V1.Paths.submissions.replace("{submissionId?}", ""),
            KJSON.stringify(SubmissionUpdateRequest.serializer(), SubmissionUpdateRequest(submission))
        )

    private suspend fun parseUserResponse(response: Response): Try<User> =
        when {
            response.ok -> Try { KJSON.parse(UserResponse.serializer(), response.text().await()).user }
            else -> {
                val errorResponse: ErrorResponse = KJSON.parse(ErrorResponse.serializer(), response.text().await())
                Failure(LoginOrRegisterFailedException(errorResponse))
            }
        }

    private suspend fun parseUserProfileResponse(response: Response): Try<UserProfileResponse> =
        when {
            response.ok -> Try { KJSON.parse(UserProfileResponse.serializer(), response.text().await()) }
            else -> {
                val errorResponse: ErrorResponse = KJSON.parse(ErrorResponse.serializer(), response.text().await())
                Failure(LoginOrRegisterFailedException(errorResponse))
            }
        }

    private suspend fun parseSubmissionResponse(response: Response): Try<Submission> =
        when {
            response.ok -> Try { KJSON.parse(SubmissionResponse.serializer(), response.text().await()).submission }
            else -> {
                val errorResponse: ErrorResponse = KJSON.parse(ErrorResponse.serializer(), response.text().await())
                Failure(LoginOrRegisterFailedException(errorResponse))
            }
        }

    class LoginOrRegisterFailedException(message: ErrorResponse) : Throwable(message.message)
}

external interface WithNetworkDataSource : RProps {
    var networkDataSource: NetworkDataSource
}
