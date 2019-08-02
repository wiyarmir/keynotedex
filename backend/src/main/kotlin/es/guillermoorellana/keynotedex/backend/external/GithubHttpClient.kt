package es.guillermoorellana.keynotedex.backend.external

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json.Companion.nonstrict
import kotlinx.serialization.list

class GithubHttpClient(
    private val httpClient: HttpClient
) {
    constructor(
        oauthToken: String?,
        httpClient: HttpClient = httpClientWithGithubToken(oauthToken)
    ) : this(httpClient)

    private val baseUrl: String = "https://api.github.com"

    suspend fun getDirectoryContent(repo: String, path: String) =
        httpClient.get<List<GithubFile>>("$baseUrl/repos/$repo/contents/$path")

    suspend fun getDirectoryFiles(repo: String, path: String): List<String> =
        getDirectoryContent(repo, path)
            .mapNotNull { it.downloadUrl }
            .map { httpClient.get<String>(it) }
}

@UseExperimental(UnstableDefault::class)
fun getHttpClientConfig(oauthToken: String? = null): HttpClientConfig<*>.() -> Unit = {
    defaultRequest {
        if (oauthToken != null) header("Authorization", "token $oauthToken")
    }
    Json {
        serializer = KotlinxSerializer(json = nonstrict).apply {
            registerList(GithubFile.serializer())
            register(GithubFile.serializer().list)
        }
    }
}

fun httpClientWithGithubToken(oauthToken: String?) = HttpClient(getHttpClientConfig(oauthToken))

@Serializable
data class GithubFile(@SerialName("download_url") val downloadUrl: String?, val path: String)
