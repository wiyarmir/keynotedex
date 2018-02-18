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

suspend fun checkSession() =
    getAndParseResult("/login", null, { parseUserProfileResponse(it) })
        .user.toModel()

suspend fun login(userId: String, password: String) =
    postAndParseResult(
        "/login",
        URLSearchParams().apply {
            append("userId", userId)
            append("password", password)
        },
        { parseUserProfileResponse(it) }
    ).user.toModel()

suspend fun logoutUser() {
    window.fetch(
        "/logout",
        object : RequestInit {
            override var method: String? = "POST"
            override var credentials: RequestCredentials? = "same-origin".asDynamic()
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
            val userResponse: UserProfileResponse = KJSON.parse(responseText)
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
            val profileResponse: UserProfileResponse = KJSON.parse(responseText)
            return profileResponse
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

suspend fun <T> getAndParseResult(url: String, body: dynamic, parse: suspend (Response) -> T): T =
    requestAndParseResult("GET", url, body, parse)

suspend fun <T> requestAndParseResult(method: String, url: String, body: dynamic, parse: suspend (Response) -> T): T {
    val response = window.fetch(url, object : RequestInit {
        override var method: String? = method
        override var body: dynamic = body
        override var credentials: RequestCredentials? = "same-origin".asDynamic()
        override var headers: dynamic = json("Accept" to "application/json")
    }).await()
    return parse(response)
}
