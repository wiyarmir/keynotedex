package es.guillermoorellana.keynotedex.web.comms

import es.guillermoorellana.keynotedex.dto.Submission
import es.guillermoorellana.keynotedex.dto.User
import es.guillermoorellana.keynotedex.responses.*
import es.guillermoorellana.keynotedex.web.model.*
import kotlinx.coroutines.experimental.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import kotlin.browser.*
import kotlin.js.*
import kotlinx.serialization.json.JSON as KJSON

suspend fun register(
    userId: String,
    password: String,
    displayName: String,
    email: String
): User = postAndParseResult(
    "/register",
    URLSearchParams().apply {
        append("userId", userId)
        append("password", password)
        append("displayName", displayName)
        append("email", email)
    },
    { parseUserProfileResponse(it).user }
)

suspend fun user(userId: String) =
    getAndParseResult("/user/$userId", null, { parseUserResponse(it) })
        .toModel()


suspend fun userProfile(userId: String) =
    getAndParseResult("/user/$userId", null, { parseUserProfileResponse(it) })
        .toModel()

suspend fun updateUserProfile(userProfile: UserProfile): UserProfile {
    val userId = userProfile.user.userId
    val body = KJSON.stringify(userProfile.toUpdateRequest())
    return putAndParseResult("/user/$userId", body, { parseUserProfileResponse(it) })
        .toModel()
}

suspend fun checkSession() =
    getAndParseResult("/login", null, { parseUserResponse(it) })
        .toModel()

suspend fun login(userId: String, password: String) =
    postAndParseResult(
        "/login",
        URLSearchParams().apply {
            append("userId", userId)
            append("password", password)
        },
        { parseUserResponse(it) }
    ).toModel()

suspend fun logoutUser() {
    window.fetch(
        "/logout",
        object : RequestInit {
            override var method: String? = "POST"
            override var credentials: RequestCredentials? = RequestCredentials.SAME_ORIGIN
        })
        .await()
}

suspend fun getSubmission(submissionId: String) =
    getAndParseResult("/submission/$submissionId", null, { parseSubmissionResponse(it) })
        .toModel()

private suspend fun parseUserResponse(response: Response): User {
    val responseText = response.text().await()
    when {
        response.ok -> {
            val userResponse: UserResponse = KJSON.parse(responseText)
            return userResponse.user
        }
        else -> {
            val errorResponse: ErrorResponse = KJSON.parse(responseText)
            throw LoginOrRegisterFailedException(errorResponse)
        }
    }
}

private suspend fun parseUserProfileResponse(response: Response): UserProfileResponse {
    val responseText = response.text().await()
    when {
        response.ok -> {
            return KJSON.parse(responseText)
        }
        else -> {
            val errorResponse: ErrorResponse = KJSON.parse(responseText)
            throw LoginOrRegisterFailedException(errorResponse)
        }
    }
}

private suspend fun parseSubmissionResponse(response: Response): Submission {
    val responseText = response.text().await()
    when {
        response.ok -> {
            val submissionResponse: SubmissionResponse = KJSON.parse(responseText)
            return submissionResponse.submission
        }
        else -> {
            val errorResponse: ErrorResponse = KJSON.parse(responseText)
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
