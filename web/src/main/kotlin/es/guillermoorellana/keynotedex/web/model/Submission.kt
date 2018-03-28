package es.guillermoorellana.keynotedex.web.model

import es.guillermoorellana.keynotedex.dto.Submission as DtoSubmission

data class Submission(
    val submissionId: String,
    val userId: String,
    val title: String,
    val abstract: String,
    val type: String,
    val submittedTo: String
)

fun DtoSubmission.toModel() = Submission(
    submissionId = submissionId,
    userId = userId,
    title = title,
    abstract = abstract ?: "",
    type = type ?: "",
    submittedTo = submittedTo ?: ""
)

fun List<DtoSubmission>.toModel(): List<Submission> = map(DtoSubmission::toModel)
