package es.guillermoorellana.keynotedex.web.model

data class IndexResponse(val top: List<Conference>)
data class LoginResponse(val user: User? = null, val error: String? = null)
