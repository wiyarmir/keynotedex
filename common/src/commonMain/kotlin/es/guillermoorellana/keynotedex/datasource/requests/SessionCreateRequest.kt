package es.guillermoorellana.keynotedex.datasource.requests

import es.guillermoorellana.keynotedex.datasource.dto.SessionVisibility
import kotlinx.serialization.Serializable

@Serializable
data class SessionCreateRequest(
    val title: String,
    val abstract: String,
    val type: String? = null,
    val submittedTo: String? = null,
    val visibility: SessionVisibility
)
