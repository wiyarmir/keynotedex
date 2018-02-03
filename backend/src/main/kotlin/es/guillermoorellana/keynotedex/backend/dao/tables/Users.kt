package es.guillermoorellana.keynotedex.backend.dao.tables

import es.guillermoorellana.keynotedex.backend.user.*
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
