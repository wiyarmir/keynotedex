package es.guillermoorellana.keynotedex.backend.data.conferences

import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.definition.primaryKey
import org.jetbrains.squash.definition.varchar

object ConferencesTable : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val name = varchar("name", 64)
}
