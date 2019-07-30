package es.guillermoorellana.keynotedex.backend.external.frontmatter

import es.guillermoorellana.keynotedex.backend.external.GithubHttpClient
import es.guillermoorellana.keynotedex.backend.frontmatter.FrontMatterParser

class GithubFrontMatterScrapper(
    private val githubHttpClient: GithubHttpClient,
    private val frontMatterParser: FrontMatterParser
) {
    suspend fun fetch(repo: String, path: String): List<Conference> =
        githubHttpClient.getDirectoryFiles(repo, path)
            .map { frontMatterParser.parse(Conference.serializer(), it) }
}
