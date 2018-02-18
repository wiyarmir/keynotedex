package es.guillermoorellana.keynotedex.backend.dao.users

import org.jetbrains.squash.definition.*

object UsersTable : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val email = varchar("email", 128).uniqueIndex().nullable()
    val displayName = varchar("display_name", 256).nullable()
    val passwordHash = varchar("password_hash", 64)
}
