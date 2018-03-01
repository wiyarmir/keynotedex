package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.dao.KeynotedexDatabase
import es.guillermoorellana.keynotedex.backend.dao.conferences.ConferencesTable
import es.guillermoorellana.keynotedex.backend.dao.submissions.SubmissionsTable
import es.guillermoorellana.keynotedex.backend.dao.users.User
import es.guillermoorellana.keynotedex.backend.dao.users.UsersTable
import io.ktor.application.ApplicationCall
import io.ktor.application.feature
import io.ktor.http.HttpHeaders
import io.ktor.locations.Locations
import io.ktor.request.header
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.response.respondRedirect
import io.ktor.util.hex
import org.jetbrains.squash.connection.transaction
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.values
import java.net.URI
import java.sql.SQLException
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val hashKey = hex("6819b57a326945c1968f45236589")
val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")
fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

suspend fun ApplicationCall.redirect(location: Any) {
    val host = request.host() ?: "localhost"
    val portSpec = request.port().let { if (it == 80) "" else ":$it" }
    val address = host + portSpec

    respondRedirect("http://$address${application.feature(Locations).href(location)}")
}

fun ApplicationCall.securityCode(date: Long, user: User, hashFunction: (String) -> String) =
    hashFunction("$date:${user.userId}:${request.host()}:${refererHost()}")

fun ApplicationCall.verifyCode(date: Long, user: User, code: String, hashFunction: (String) -> String) =
    securityCode(date, user, hashFunction) == code
            && (System.currentTimeMillis() - date).let {
        it > 0 && it < TimeUnit.MILLISECONDS.convert(
            2,
            TimeUnit.HOURS
        )
    }

private val userIdPattern = "[a-zA-Z0-9_.]+".toRegex()
internal fun userNameValid(userId: String) = userId.matches(userIdPattern)

internal fun KeynotedexDatabase.mockData() {
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
                        it[passwordHash] = hash("user$i!")
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
