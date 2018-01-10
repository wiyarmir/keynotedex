package es.guillermoorellana.conferences.backend.model.responses

import es.guillermoorellana.conferences.backend.model.Conference

interface JsonResponse

data class IndexResponse(val conferences: List<Conference>) : JsonResponse
