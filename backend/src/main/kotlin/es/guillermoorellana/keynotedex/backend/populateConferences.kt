package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.external.GithubConferenceScrapper
import es.guillermoorellana.keynotedex.backend.external.toDao
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI

@UseExperimental(KtorExperimentalAPI::class)
suspend fun Application.populateConferences(database: KeynotedexStorage) = database.apply {
    if (conferences().isNotEmpty()) return@apply
    environment.config.configList("keynotedex.conferences.frontload")
        .map { it.property("repo").getString() to it.property("path").getString() }
        .flatMap { (repo, path) ->
            environment.log.debug("Loading conferences at repo $repo from path $path")
            GithubConferenceScrapper(
                oauthToken = environment.config.propertyOrNull("keynotedex.oauth.github.token")?.getString()
            )
                .fetch(repo, path)
        }
        .map { it.toDao() }
        .toList()
        .also { environment.log.debug("Loading ${it.size} conferences into the database") }
        .let { conferences ->
            database.putAll(conferences)
            environment.log.debug("Done loading conferences")
        }
}
