package es.guillermoorellana.keynotedex.web.model

data class Submission(
    val submissionId: String,
    val title: String,
    val abstract: String,
    val type: String,
    val submittedTo: String
)

fun es.guillermoorellana.keynotedex.dto.Submission.toModel() = Submission(
    submissionId = submissionId,
    title = title,
    abstract = abstract ?: "",
    type = type ?: "",
    submittedTo = submittedTo ?: ""
)
