package es.guillermoorellana.keynotedex.backend.dao.conferences

import org.jetbrains.squash.results.*

fun transformConference(it: ResultRow): Conference =
    Conference(
        name = it[Conferences.name]
    )

fun Conference.toDto() = es.guillermoorellana.keynotedex.dto.Conference(
    name = name
)
