package es.guillermoorellana.keynotedex.backend.index

import es.guillermoorellana.keynotedex.backend.conference.Conference

data class IndexResponse(val conferences: List<Conference>)
