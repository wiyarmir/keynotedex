package es.guillermoorellana.keynotedex.web.comms

import es.guillermoorellana.keynotedex.web.model.*
import kotlinx.coroutines.experimental.await
import org.w3c.dom.url.URLSearchParams
import org.w3c.fetch.*
import kotlin.browser.window
import kotlin.js.*

suspend fun index(): IndexResponse =
    getAndParseResult("/", null, { parseIndexResponse(it) })

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
    { parseLoginOrRegisterResponse(it) }
)

suspend fun checkSession(): User =
    getAndParseResult("/login", null, { parseLoginOrRegisterResponse(it) })

suspend fun login(userId: String, password: String): User =
    postAndParseResult(
        "/login",
        URLSearchParams().apply {
            append("userId", userId)
            append("password", password)
        },
        { parseLoginOrRegisterResponse(it) }
    )

suspend fun logoutUser() {
    window.fetch(
        "/logout",
        object : RequestInit {
            override var method: String? = "POST"
            override var credentials: RequestCredentials? = "same-origin".asDynamic()
        })
        .await()
}

private fun parseIndexResponse(json: dynamic): IndexResponse {
    val top = json.top as Array<dynamic>

    return IndexResponse(top.map(::parseConference))
}

private fun parseConference(json: dynamic): Conference {
    return Conference(json.name as String)
}

private suspend fun parseLoginOrRegisterResponse(response: Response): User {
    val json: dynamic = response.json().await()

    if (response.ok) {
        return User(
            json.user.userId as String,
            json.user.displayName as String?
        )
    } else {
        throw LoginOrRegisterFailedException(json.message.toString())
    }
}

class LoginOrRegisterFailedException(message: String) : Throwable(message)

suspend fun <T> postAndParseResult(url: String, body: dynamic, parse: suspend (dynamic) -> T): T =
    requestAndParseResult("POST", url, body, parse)

suspend fun <T> getAndParseResult(url: String, body: dynamic, parse: suspend (dynamic) -> T): T =
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
