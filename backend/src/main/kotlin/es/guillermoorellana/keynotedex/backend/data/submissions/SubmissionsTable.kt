package es.guillermoorellana.keynotedex.backend.data.submissions

import org.jetbrains.squash.definition.*

object SubmissionsTable : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val public = bool("public")
    val submitter = varchar("submitter", 20).index()
    val title = varchar("title", 200)
    val abstract = varchar("abstract", 2000).nullable()
}

