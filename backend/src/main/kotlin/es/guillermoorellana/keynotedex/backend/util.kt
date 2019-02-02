package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.data.KeynotedexDatabase
import es.guillermoorellana.keynotedex.backend.data.conferences.ConferencesTable
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionsTable
import es.guillermoorellana.keynotedex.backend.data.users.UsersTable
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.feature
import io.ktor.http.HttpHeaders
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.request.header
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.response.respondRedirect
import org.jetbrains.squash.connection.transaction
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.values
import java.net.URI
import java.sql.SQLException

fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

suspend fun ApplicationCall.redirect(location: Any) = respondRedirect(redirectString(location))

@UseExperimental(KtorExperimentalLocationsAPI::class)
fun ApplicationCall.redirectString(location: Any): String {
    val host = request.host() ?: "localhost"
    val portSpec = request.port().let { if (it == 80) "" else ":$it" }
    val address = host + portSpec

    return "http://$address${application.feature(Locations).href(location)}"
}

internal fun KeynotedexDatabase.mockData(application: Application) {
    try {
        db.transaction {
            for (i in 1..10) {
                insertInto(ConferencesTable)
                    .values { statement ->
                        statement[id] = "$i"
                        statement[name] = "Conference$i"
                    }
                    .execute()

                insertInto(UsersTable)
                    .values { statement ->
                        statement[id] = "user$i"
                        statement[displayName] = "User #$i"
                        statement[email] = if (i % 2 == 0) "userId$i@keynotedex.co" else null
                        statement[passwordHash] = application.hashPassword("user$i!")
                    }
                    .execute()

                insertInto(SubmissionsTable)
                    .values { statement ->
                        statement[public] = i % 2 == 0
                        statement[submitter] = "user1"
                        statement[title] = "My talk $i"
                        statement[abstract] = "This talk is about talks.\nSo it is a meta talk."
                    }
                    .execute()

                insertInto(SubmissionsTable)
                    .values { statement ->
                        statement[public] = i % 2 == 0
                        statement[submitter] = "user2"
                        statement[title] = "My talk $i"
                        statement[abstract] = "This talk is about talks.\nSo it is a meta talk."
                    }
                    .execute()
            }
        }
    } catch (e: SQLException) {
        println("Problem mocking data: $e")
    }
}
