package es.guillermoorellana.keynotedex.web.comms

import es.guillermoorellana.keynotedex.api.Api
import es.guillermoorellana.keynotedex.dto.Submission
import es.guillermoorellana.keynotedex.dto.User
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
import kotlin.js.json
import kotlinx.serialization.json.JSON as KJSON

suspend fun register(
    userId: String,
    password: String,
    displayName: String,
    email: String
): User = postAndParseResult(
    Api.V1.Paths.register,
    URLSearchParams().apply {
        append("userId", userId)
        append("password", password)
        append("displayName", displayName)
        append("email", email)
    }
) { parseUserProfileResponse(it).user }

suspend fun userProfile(userId: String) =
    getAndParseResult(Api.V1.Paths.user.replace("{userId}", userId), null) { parseUserProfileResponse(it) }
        .toModel()

suspend fun updateUserProfile(userProfile: UserProfile): UserProfile {
    val userId = userProfile.user.userId
    val body = KJSON.stringify(UserProfileUpdateRequest.serializer(), userProfile.toUpdateRequest())
    return putAndParseResult(Api.V1.Paths.user.replace("{userId}", userId), body) { parseUserProfileResponse(it) }
        .toModel()
}

suspend fun checkSession() =
    getAndParseResult(Api.V1.Paths.login, null) { parseUserResponse(it) }
        .toModel()

suspend fun login(userId: String, password: String) =
    postAndParseResult(
        Api.V1.Paths.login,
        URLSearchParams().apply {
            append("userId", userId)
            append("password", password)
        }
    ) { parseUserResponse(it) }.toModel()

suspend fun logoutUser() =
    window.fetch(
        Api.V1.Paths.logout,
        object : RequestInit {
            override var method: String? = "POST"
            override var credentials: RequestCredentials? = RequestCredentials.SAME_ORIGIN
        })
        .await()

suspend fun getSubmission(submissionId: String) =
    getAndParseResult(
        Api.V1.Paths.submissions.replace("{submissionId}", submissionId),
        null
    ) { parseSubmissionResponse(it) }
        .toModel()

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

private suspend fun parseUserProfileResponse(response: Response): UserProfileResponse {
    val responseText = response.text().await()
    when {
        response.ok -> {
            return KJSON.parse(UserProfileResponse.serializer(), responseText)
        }
        else -> {
            val errorResponse: ErrorResponse = KJSON.parse(ErrorResponse.serializer(), responseText)
            throw LoginOrRegisterFailedException(errorResponse)
        }
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

suspend fun <T> postAndParseResult(url: String, body: dynamic, parse: suspend (Response) -> T): T =
    requestAndParseResult("POST", url, body, parse)

suspend fun <T> putAndParseResult(url: String, body: dynamic, parse: suspend (Response) -> T): T =
    requestAndParseResult("PUT", url, body, parse)

suspend fun <T> getAndParseResult(url: String, body: dynamic, parse: suspend (Response) -> T): T =
    requestAndParseResult("GET", url, body, parse)

suspend fun <T> requestAndParseResult(method: String, url: String, body: dynamic, parse: suspend (Response) -> T): T {
    val headers =
        mutableListOf("Accept" to "application/json")
            // TODO rewrite, so ugly
            .apply { if (body != null && body !is URLSearchParams) add("Content-Type" to "application/json") }
            .toTypedArray()
    val request: RequestInit = object : RequestInit {
        override var method: String? = method
        override var body: dynamic = body
        override var credentials: RequestCredentials? = RequestCredentials.SAME_ORIGIN
        override var headers: dynamic = json(*headers)
    }
    val response = window.fetch(url, request).await()
    return parse(response)
}
