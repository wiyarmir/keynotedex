package es.guillermoorellana.keynotedex.backend.external

import es.guillermoorellana.keynotedex.backend.frontmatter.FrontMatterParser
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json.Companion.nonstrict
import kotlinx.serialization.list

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

class GithubFrontMatterScrapper(
    private val httpClient: HttpClient,
    private val frontMatterParser: FrontMatterParser
) {
    constructor(
        oauthToken: String? = null,
        httpClient: HttpClient = HttpClient(getHttpClientConfig(oauthToken)),
        frontMatterParser: FrontMatterParser = FrontMatterParser()
    ) : this(httpClient, frontMatterParser)

    private val baseUrl: String = "https://api.github.com"

    suspend fun fetch(repo: String, path: String): List<Conference> =
        getDirectoryContent(repo, path)
            .map { it.downloadUrl }
            .map { httpClient.get<String>(it) }
            .map { frontMatterParser.parse(Conference.serializer(), it) }

    private suspend fun getDirectoryContent(repo: String, path: String) =
        httpClient.get<List<GithubFile>>("$baseUrl/repos/$repo/contents/$path")
}

@Serializable
data class GithubFile(@SerialName("download_url") val downloadUrl: String)
