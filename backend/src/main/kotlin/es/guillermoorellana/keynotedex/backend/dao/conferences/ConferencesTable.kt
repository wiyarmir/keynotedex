package es.guillermoorellana.keynotedex.backend.dao.conferences

import org.jetbrains.squash.definition.*

object ConferencesTable : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val name = varchar("name", 64)
}