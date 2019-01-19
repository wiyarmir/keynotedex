package es.guillermoorellana.keynotedex.web.comms

import arrow.core.Try
import arrow.core.orNull
import es.guillermoorellana.keynotedex.api.Api
import es.guillermoorellana.keynotedex.dto.Submission
import es.guillermoorellana.keynotedex.dto.User
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
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import org.w3c.fetch.SAME_ORIGIN
import kotlin.browser.window
import kotlinx.serialization.json.JSON as KJSON

object NetworkDataSource {
    private val networkService = NetworkService

    suspend fun register(userId: String, password: String, displayName: String, email: String) =
        networkService.postAndParseResult(
            Api.V1.Paths.register,
            URLSearchParams().apply {
                append("userId", userId)
                append("password", password)
                append("displayName", displayName)
                append("email", email)
            }
        ) { parseUserProfileResponse(it).user }
            .map { it.toModel() }
            .orNull()!!

    suspend fun userProfile(userId: String): Try<UserProfile> =
        networkService.getAndParseResult(
            Api.V1.Paths.user.replace("{userId}", userId),
            null
        ) { parseUserProfileResponse(it) }
            .map { it.toModel() }

    suspend fun updateUserProfile(userProfile: UserProfile): UserProfile? {
        val userId = userProfile.user.userId
        val body = KJSON.stringify(UserProfileUpdateRequest.serializer(), userProfile.toUpdateRequest())
        return networkService.putAndParseResult(
            Api.V1.Paths.user.replace("{userId}", userId),
            body
        ) { parseUserProfileResponse(it) }
            .map { it.toModel() }
            .orNull()
    }

    suspend fun checkSession() =
        networkService.getAndParseResult(Api.V1.Paths.login, null) { parseUserResponse(it) }
            .map { it.toModel() }
            .orNull()

    suspend fun login(userId: String, password: String) =
        networkService.postAndParseResult(
            Api.V1.Paths.login,
            URLSearchParams().apply {
                append("userId", userId)
                append("password", password)
            }
        ) { parseUserResponse(it) }
            .map { it.toModel() }
            .orNull()

    suspend fun logoutUser() =
        window.fetch(
            Api.V1.Paths.logout,
            object : RequestInit {
                override var method: String? = "POST"
                override var credentials: RequestCredentials? = RequestCredentials.SAME_ORIGIN
            })
            .await()

    suspend fun getSubmission(submissionId: String) =
        networkService.getAndParseResult(
            Api.V1.Paths.submissions.replace("{submissionId?}", submissionId),
            null
        ) { parseSubmissionResponse(it) }
            .map { it.toModel() }
            .orNull()

    suspend fun postSubmission(submission: Submission) =
        networkService.postAndParseResult(
            Api.V1.Paths.submissions.replace("{submissionId?}", ""),
            submission
        ) {}

    suspend fun updateSubmission(submission: Submission) =
        networkService.putAndParseResult(
            Api.V1.Paths.submissions.replace("{submissionId?}", ""),
            KJSON.stringify(SubmissionUpdateRequest.serializer(), SubmissionUpdateRequest(submission))
        ) {}

    private suspend fun parseUserResponse(response: Response): User {
        val responseText = response.text().await()
        when {
            response.ok -> {
                val userResponse: UserResponse = KJSON.parse(UserResponse.serializer(), responseText)
                return userResponse.user
            }
            else -> {
                val errorResponse: ErrorResponse = KJSON.parse(ErrorResponse.serializer(), responseText)
                throw LoginOrRegisterFailedException(errorResponse)
            }
        }
    }

    private suspend fun parseUserProfileResponse(response: Response): UserProfileResponse = when {
        response.ok -> KJSON.parse(UserProfileResponse.serializer(), response.text().await())
        else -> {
            val errorResponse: ErrorResponse = KJSON.parse(ErrorResponse.serializer(), response.text().await())
            throw LoginOrRegisterFailedException(errorResponse)
        }
    }

    private suspend fun parseSubmissionResponse(response: Response): Submission {
        val responseText = response.text().await()
        when {
            response.ok -> {
                val submissionResponse: SubmissionResponse = KJSON.parse(SubmissionResponse.serializer(), responseText)
                return submissionResponse.submission
            }
            else -> {
                val errorResponse: ErrorResponse = KJSON.parse(ErrorResponse.serializer(), responseText)
                throw LoginOrRegisterFailedException(errorResponse)
            }
        }
    }

    class LoginOrRegisterFailedException(message: ErrorResponse) : Throwable(message.message)
}
