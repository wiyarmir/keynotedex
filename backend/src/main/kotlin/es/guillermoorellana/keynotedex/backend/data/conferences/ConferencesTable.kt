package es.guillermoorellana.keynotedex.backend.data.conferences

import org.jetbrains.squash.definition.*

object ConferencesTable : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val name = varchar("name", 64)
}
