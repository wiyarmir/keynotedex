package es.guillermoorellana.keynotedex.dto

import kotlinx.serialization.Serializable

@Serializable
data class User(val userId: String, val displayName: String?)
