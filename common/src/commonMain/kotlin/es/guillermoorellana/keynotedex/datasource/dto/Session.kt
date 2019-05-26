package es.guillermoorellana.keynotedex.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val userId: String = "",
    val sessionId: String = "",
    val title: String,
    val abstract: String? = null,
    val type: String? = null,
    val submittedTo: String? = null,
    val visibility: SessionVisibility
)

enum class SessionVisibility {
    PRIVATE, PUBLIC
}
