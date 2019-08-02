package es.guillermoorellana.keynotedex.backend.external

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class GithubHttpClientTest {
    private val server = MockWebServer()

    @Before
    fun setUp() {
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun testDirectoryContent() = runBlocking {
        server.enqueue(
            MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(fromClasspath("/github/mocks/_conferences.json"))
        )

        val serverUrl = server.url("/")

        val client = GithubHttpClient(
            httpClient = HttpClient(
                OkHttp.create { addInterceptor(rewriteUrlInterceptor(serverUrl)) },
                getHttpClientConfig()
            )
        )

        val directory = client.getDirectoryContent("npatarino/tech-conferences-spain", "_conferences/")

        assert(directory.size == 1)
    }

    @Test
    fun testDirectoryFiles() = runBlocking {
        server.enqueue(
            MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(fromClasspath("/github/mocks/_conferences.json"))
        )

        server.enqueue(
            MockResponse()
                .addHeader("Content-Type", "text/plain; charset=utf-8")
                .setBody(fromClasspath("/frontmatter/complete.md"))
        )

        val serverUrl = server.url("/")

        val client = GithubHttpClient(
            httpClient = HttpClient(
                OkHttp.create { addInterceptor(rewriteUrlInterceptor(serverUrl)) },
                getHttpClientConfig()
            )
        )

        val files = client.getDirectoryFiles("npatarino/tech-conferences-spain", "_conferences/")

        assert(files.size == 1)
    }

    private fun fromClasspath(path: String) = javaClass.getResourceAsStream(path).bufferedReader().readText()
}

val rewriteUrlInterceptor = { serverUrl: HttpUrl ->
    Interceptor {
        val request = it.request()

        val url = request.url
            .newBuilder()
            .host(serverUrl.host)
            .port(serverUrl.port)
            .scheme("http")
            .build()

        it.proceed(
            request.newBuilder()
                .url(url)
                .build()
        )
    }
}
