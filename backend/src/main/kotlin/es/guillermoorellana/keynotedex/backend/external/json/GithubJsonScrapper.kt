package es.guillermoorellana.keynotedex.backend.external.json

import es.guillermoorellana.keynotedex.backend.external.GithubHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class GithubJsonScrapper(
    private val githubHttpClient: GithubHttpClient,
    private val json: Json = Json.nonstrict,
    private val httpClient: HttpClient = HttpClient()
) {
    suspend fun fetchAllYears(repo: String, path: String) =
        githubHttpClient.getDirectoryContent(repo, path)
            .flatMap { githubHttpClient.getDirectoryContent(repo, it.path) }
            .mapNotNull { it.downloadUrl }
            .map { httpClient.get<String>(it) }
            .map { it.replace("cpfUrl", "cfpUrl") }
            .flatMap { json.parse(Conference.serializer().list, it) }
}
