package es.guillermoorellana.keynotedex.backend.dao.tables

import es.guillermoorellana.keynotedex.backend.conference.*
import org.jetbrains.squash.definition.*
import org.jetbrains.squash.results.*

object Conferences : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val name = varchar("name", 64)
}

fun transformConference(it: ResultRow): Conference = Conference(
    name = it[Conferences.name]
)
