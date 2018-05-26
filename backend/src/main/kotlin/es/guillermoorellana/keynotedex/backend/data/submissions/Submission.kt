package es.guillermoorellana.keynotedex.backend.data.submissions

data class Submission(
    val id: String,
    val title: String,
    val abstract: String?,
    val isPublic: Boolean,
    val submitterId: String
)
