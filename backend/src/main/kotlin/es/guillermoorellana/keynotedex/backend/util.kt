package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.dao.KeynotedexDatabase
import es.guillermoorellana.keynotedex.backend.dao.conferences.ConferencesTable
import es.guillermoorellana.keynotedex.backend.dao.submissions.SubmissionsTable
import es.guillermoorellana.keynotedex.backend.dao.users.UsersTable
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.feature
import io.ktor.http.HttpHeaders
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

suspend fun ApplicationCall.redirect(location: Any) {
    val host = request.host() ?: "localhost"
    val portSpec = request.port().let { if (it == 80) "" else ":$it" }
    val address = host + portSpec

    respondRedirect("http://$address${application.feature(Locations).href(location)}")
}

private val userIdPattern = "[a-zA-Z0-9_.]+".toRegex()
internal fun userNameValid(userId: String) = userId.matches(userIdPattern)

internal fun KeynotedexDatabase.mockData(application: Application) {
    try {
        db.transaction {
            for (i in 1..10) {
                insertInto(ConferencesTable)
                    .values {
                        it[id] = "$i"
                        it[name] = "Conference$i"
                    }
                    .execute()

                insertInto(UsersTable)
                    .values {
                        it[id] = "user$i"
                        it[displayName] = "User #$i"
                        it[email] = if (i % 2 == 0) "userId$i@keynotedex.co" else null
                        it[passwordHash] = application.hashPassword("user$i!")
                    }
                    .execute()

                insertInto(SubmissionsTable)
                    .values {
                        it[id] = "$i"
                        it[public] = i % 2 == 0
                        it[submitter] = "user1"
                        it[title] = "My talk $i"
                        it[abstract] = "This talk is about talks.\nSo it is a meta talk."
                    }
                    .execute()

                insertInto(SubmissionsTable)
                    .values {
                        it[id] = "2$i"
                        it[public] = i % 2 == 0
                        it[submitter] = "user2"
                        it[title] = "My talk $i"
                        it[abstract] = "This talk is about talks.\nSo it is a meta talk."
                    }
                    .execute()
            }
        }
    } catch (e: SQLException) {
        println("Problem mocking data")
    }
}
