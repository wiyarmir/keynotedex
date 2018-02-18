package es.guillermoorellana.keynotedex.backend.dao.submissions

data class Submission(
    val id: String,
    val title: String,
    val abstract: String?,
    val isPublic: Boolean,
    val submitterId: String
)
