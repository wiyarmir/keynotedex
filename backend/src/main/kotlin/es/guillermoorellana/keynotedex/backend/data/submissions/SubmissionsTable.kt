package es.guillermoorellana.keynotedex.backend.data.submissions

import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.definition.autoIncrement
import org.jetbrains.squash.definition.bool
import org.jetbrains.squash.definition.index
import org.jetbrains.squash.definition.long
import org.jetbrains.squash.definition.nullable
import org.jetbrains.squash.definition.primaryKey
import org.jetbrains.squash.definition.varchar

object SubmissionsTable : TableDefinition() {
    val id = long("id").primaryKey().autoIncrement()
    val public = bool("public")
    val submitter = varchar("submitter", 20).index()
    val title = varchar("title", 200)
    val abstract = varchar("abstract", 2000).nullable()
}
