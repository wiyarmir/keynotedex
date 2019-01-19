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

    suspend fun <T> postAndParseResult(url: String, body: dynamic, parse: suspend (Response) -> T): Try<T> =
        requestAndParseResult("POST", url, body, parse)

    suspend fun <T> putAndParseResult(url: String, body: dynamic, parse: suspend (Response) -> T): Try<T> =
        requestAndParseResult("PUT", url, body, parse)

    suspend fun <T> getAndParseResult(url: String, body: dynamic, parse: suspend (Response) -> T): Try<T> =
        requestAndParseResult("GET", url, body, parse)

    private suspend fun <T> requestAndParseResult(
        method: String,
        url: String,
        body: dynamic,
        parse: suspend (Response) -> T
    ): Try<T> {
        val headers =
            if (body == null || body is URLSearchParams) {
                listOf("Accept" to "application/json")
            } else {
                listOf(
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
        }.map { parse(it) }
    }
}
