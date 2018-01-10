package es.guillermoorellana.keynotedex.backend.dao.tables

import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.definition.primaryKey
import org.jetbrains.squash.definition.uniqueIndex
import org.jetbrains.squash.definition.varchar

object Users : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val email = varchar("email", 128).uniqueIndex()
    val displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)
}
