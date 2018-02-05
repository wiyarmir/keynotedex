package es.guillermoorellana.keynotedex.backend.dao.tables

import org.jetbrains.squash.definition.*
import org.jetbrains.squash.results.*

object Conferences : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val name = varchar("name", 64)
}

data class Conference(val name: String)

fun transformConference(it: ResultRow): Conference =
    Conference(
        name = it[Conferences.name]
    )

fun Conference.toDto() = es.guillermoorellana.keynotedex.dto.Conference(
    name = name
)
