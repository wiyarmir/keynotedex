package es.guillermoorellana.keynotedex.backend.data.conferences

import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.definition.autoIncrement
import org.jetbrains.squash.definition.long
import org.jetbrains.squash.definition.primaryKey
import org.jetbrains.squash.definition.varchar

object ConferencesTable : TableDefinition() {
    val id = long("id").primaryKey().autoIncrement()
    val name = varchar("name", 64)
}
