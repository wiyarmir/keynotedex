package es.guillermoorellana.keynotedex.web

import es.guillermoorellana.keynotedex.web.model.*
import kotlinx.coroutines.experimental.await
import org.w3c.dom.url.URLSearchParams
import org.w3c.fetch.*
import kotlin.browser.window
import kotlin.js.*

suspend fun index(): IndexResponse = getAndParseResult("/", null, ::parseIndexResponse)

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
        ::parseLoginOrRegisterResponse)

suspend fun pollFromLastTime(lastTime: String = ""): String =
        getAndParseResult<String>("/poll?lastTime=$lastTime", null, { json ->
            json.count
        })

suspend fun checkSession(): User =
        getAndParseResult("/login", null, ::parseLoginOrRegisterResponse)

suspend fun login(userId: String, password: String): User =
        postAndParseResult("/login", URLSearchParams().apply {
            append("userId", userId)
            append("password", password)
        }, ::parseLoginOrRegisterResponse)

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

private fun parseLoginOrRegisterResponse(json: dynamic): User {
    if (json.error != null) {
        throw LoginOrRegisterFailedException(json.error.toString())
    }

    return User(
            json.user.userId as String,
            json.user.email as String,
            json.user.displayName as String,
            json.user.passwordHash as String
    )
}

class LoginOrRegisterFailedException(message: String) : Throwable(message)

suspend fun <T> postAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("POST", url, body, parse)

suspend fun <T> getAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("GET", url, body, parse)

suspend fun <T> requestAndParseResult(method: String, url: String, body: dynamic, parse: (dynamic) -> T): T {
    val response = window.fetch(url, object : RequestInit {
        override var method: String? = method
        override var body: dynamic = body
        override var credentials: RequestCredentials? = "same-origin".asDynamic()
        override var headers: dynamic = json("Accept" to "application/json")
    }).await()
    return parse(response.json().await())
}
