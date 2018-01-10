package es.guillermoorellana.keynotedex.backend.dao.tables

import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.definition.primaryKey
import org.jetbrains.squash.definition.varchar

object Conferences : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val name = varchar("name", 64)
}
