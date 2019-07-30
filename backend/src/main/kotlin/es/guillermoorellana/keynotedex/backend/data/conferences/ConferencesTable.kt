package es.guillermoorellana.keynotedex.backend.data.conferences

import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.definition.autoIncrement
import org.jetbrains.squash.definition.long
import org.jetbrains.squash.definition.nullable
import org.jetbrains.squash.definition.primaryKey
import org.jetbrains.squash.definition.varchar

object ConferencesTable : TableDefinition() {
    val id = long("id").primaryKey().autoIncrement()
    val name = varchar("name", 256)
    val dateStart = varchar("dateStart", 64)
    val dateEnd = varchar("dateEnd", 64)
    val location = varchar("location", 64).nullable()
    val website = varchar("website", 256).nullable()
    val twitter = varchar("twitter", 64).nullable()
    val cfpStart = varchar("cfpStart", 64).nullable()
    val cfpEnd = varchar("cfpEnd", 64).nullable()
    val cfpSite = varchar("cfpSite", 256).nullable()
}
