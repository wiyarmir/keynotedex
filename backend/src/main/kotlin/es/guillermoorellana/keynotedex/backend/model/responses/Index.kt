package es.guillermoorellana.keynotedex.backend.model.responses

import es.guillermoorellana.keynotedex.backend.model.Conference

interface JsonResponse

data class IndexResponse(val conferences: List<Conference>) : JsonResponse
