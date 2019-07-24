package es.guillermoorellana.keynotedex.backend.data.conferences

import org.jetbrains.squash.results.ResultRow
import org.jetbrains.squash.results.get
import es.guillermoorellana.keynotedex.datasource.dto.Conference as DtoConference

fun transformConference(it: ResultRow): Conference =
    Conference(
        name = it[ConferencesTable.name]
    )

fun Conference.toDto() = DtoConference(
    name = name
)

fun List<Conference>.toDto() = map { it.toDto() }
