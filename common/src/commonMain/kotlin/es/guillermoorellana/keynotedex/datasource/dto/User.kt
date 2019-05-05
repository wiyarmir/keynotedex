package es.guillermoorellana.keynotedex.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class User(val userId: String, val displayName: String?, val bio: String?)
