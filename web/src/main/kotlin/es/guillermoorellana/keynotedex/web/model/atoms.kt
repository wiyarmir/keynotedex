package es.guillermoorellana.keynotedex.web.model

data class Conference(val name: String)
data class User(val userId: String, val email: String, val displayName: String, val passwordHash: String)
