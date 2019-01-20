package es.guillermoorellana.keynotedex.web.comms

import arrow.core.Try
import kotlinx.coroutines.await
import org.w3c.dom.url.URLSearchParams
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import org.w3c.fetch.SAME_ORIGIN
import kotlin.browser.window
import kotlin.js.json

object NetworkService {

    suspend fun post(url: String, body: dynamic) = request("POST", url, body)

    suspend fun put(url: String, body: dynamic) = request("PUT", url, body)

    suspend fun get(url: String, body: dynamic) = request("GET", url, body)

    private suspend fun request(method: String, url: String, body: dynamic): Try<Response> {
        val headers = when (body) {
            null, is URLSearchParams -> listOf("Accept" to "application/json")
            else -> listOf(
                "Accept" to "application/json",
                "Content-Type" to "application/json"
            )
        }

        val request: RequestInit = object : RequestInit {
            override var method: String? = method
            override var body: dynamic = body
            override var credentials: RequestCredentials? = RequestCredentials.SAME_ORIGIN
            override var headers: dynamic = json(*headers.toTypedArray())
        }

        return Try {
            window.fetch(url, request).await()
        }
    }
}
