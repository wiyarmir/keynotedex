package es.guillermoorellana.keynotedex.dto

import kotlinx.serialization.Serializable

@Serializable
data class Submission(
    val submissionId: String,
    val title: String,
    val abstract: String? = null,
    val type: String? = null,
    val submittedTo: String? = null
)
