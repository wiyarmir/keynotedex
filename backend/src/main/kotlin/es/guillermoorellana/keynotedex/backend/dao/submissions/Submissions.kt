package es.guillermoorellana.keynotedex.backend.dao.submissions

import org.jetbrains.squash.definition.*

object Submissions : TableDefinition() {
    val id = varchar("id", 20).primaryKey()
    val submitter = varchar("submitter", 20).index()
    val title = varchar("title", 200)
    val abstract = varchar("abstract", 2000).nullable()
}

