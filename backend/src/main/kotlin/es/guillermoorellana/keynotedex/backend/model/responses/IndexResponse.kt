package es.guillermoorellana.keynotedex.backend.model.responses

import es.guillermoorellana.keynotedex.backend.model.Conference

data class IndexResponse(val conferences: List<Conference>)
