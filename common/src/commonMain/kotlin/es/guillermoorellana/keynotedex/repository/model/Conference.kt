package es.guillermoorellana.keynotedex.repository.model

import es.guillermoorellana.keynotedex.datasource.dto.Conference as DtoConference

data class Conference(val name: String)

fun DtoConference.toModel(): Conference = Conference(
    name = name
)
