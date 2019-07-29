package es.guillermoorellana.keynotedex.backend.data.conferences

import org.jetbrains.squash.results.ResultRow
import org.jetbrains.squash.results.get
import es.guillermoorellana.keynotedex.datasource.dto.Conference as DtoConference

fun transformConference(it: ResultRow): Conference =
    Conference(
        name = it[ConferencesTable.name],
        dateStart = it[ConferencesTable.dateStart],
        dateEnd = it[ConferencesTable.dateEnd],
        location = it[ConferencesTable.location],
        website = it[ConferencesTable.website],
        twitter = it[ConferencesTable.twitter],
        cfpStart = it[ConferencesTable.cfpStart],
        cfpEnd = it[ConferencesTable.cfpEnd],
        cfpSite = it[ConferencesTable.cfpSite]
    )

fun Conference.toDto() = DtoConference(
    name = name,
    dateStart = dateStart,
    dateEnd = dateEnd,
    location = location,
    website = website,
    twitter = twitter,
    cfpStart = cfpStart,
    cfpEnd = cfpEnd,
    cfpSite = cfpSite
)

fun List<Conference>.toDto() = map { it.toDto() }
