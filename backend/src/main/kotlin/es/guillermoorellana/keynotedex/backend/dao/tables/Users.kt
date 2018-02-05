package es.guillermoorellana.keynotedex.backend.dao.tables

import org.jetbrains.squash.definition.*
import org.jetbrains.squash.results.*

object Users : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val email = varchar("email", 128).uniqueIndex().nullable()
    val displayName = varchar("display_name", 256).nullable()
    val passwordHash = varchar("password_hash", 64)
}

fun transformUser(it: ResultRow): User =
    User(
        it[Users.id],
        it[Users.email],
        it[Users.displayName],
        it[Users.passwordHash]
    )

data class User(
    val userId: String,
    val email: String? = null,
    val displayName: String? = null,
    val passwordHash: String,
    val submissions: List<Submission> = emptyList()
)

fun User.toDto() =
    es.guillermoorellana.keynotedex.dto.User(
        userId = userId,
        displayName = displayName,
        submissions = submissions.map { it.toDto() }
    )
