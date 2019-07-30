@file:UseExperimental(KtorExperimentalAPI::class)

package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.data.conferences.Conference
import es.guillermoorellana.keynotedex.backend.external.GithubHttpClient
import es.guillermoorellana.keynotedex.backend.external.frontmatter.GithubFrontMatterScrapper
import es.guillermoorellana.keynotedex.backend.external.frontmatter.toDao
import es.guillermoorellana.keynotedex.backend.external.json.GithubJsonScrapper
import es.guillermoorellana.keynotedex.backend.external.json.toDao
import es.guillermoorellana.keynotedex.backend.frontmatter.FrontMatterParser
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI

suspend fun Application.populateConferences(storage: KeynotedexStorage) = storage.apply {
    if (conferences().isNotEmpty()) return@apply
    val oauthToken = environment.config.propertyOrNull("keynotedex.oauth.github.token")?.getString()
    val githubHttpClient = GithubHttpClient(oauthToken)
    listOf(
        jsonConferences(githubHttpClient),
        frontmatterConferences(githubHttpClient)
    )
        .flatten()
        .also { environment.log.debug("Loading ${it.size} conferences into the database") }
        .let { conferences ->
            storage.putAll(conferences)
            environment.log.debug("Done loading conferences")
        }
}

private suspend fun Application.frontmatterConferences(githubHttpClient: GithubHttpClient): List<Conference> =
    environment.config.configList("keynotedex.conferences.frontmatter")
        .map { it.property("repo").getString() to it.property("path").getString() }
        .flatMap { (repo, path) ->
            environment.log.debug("Loading conferences at repo $repo from path $path")
            GithubFrontMatterScrapper(
                githubHttpClient = githubHttpClient,
                frontMatterParser = FrontMatterParser()
            ).fetch(repo, path)
        }
        .map { it.toDao() }

private suspend fun Application.jsonConferences(githubHttpClient: GithubHttpClient): List<Conference> =
    environment.config.configList("keynotedex.conferences.json")
        .map { it.property("repo").getString() to it.property("path").getString() }
        .flatMap { (repo, path) ->
            environment.log.debug("Loading conferences at repo $repo from path $path")
            GithubJsonScrapper(
                githubHttpClient = githubHttpClient
            ).fetchAllYears(repo, path)
        }
        .map { it.toDao() }
