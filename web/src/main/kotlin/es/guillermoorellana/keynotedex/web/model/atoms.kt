package es.guillermoorellana.keynotedex.web.model

data class User(
    val userId: String,
    val displayName: String,
    val submissions: List<Submission>
)

fun es.guillermoorellana.keynotedex.dto.User.toModel(): User =
    User(
        userId = userId,
        displayName = displayName ?: userId,
        submissions = submissions.map { it.toModel() }
    )

data class Submission(
    val title: String,
    val abstract: String,
    val type: String,
    val submittedTo: String
)

fun es.guillermoorellana.keynotedex.dto.Submission.toModel(): Submission =
    Submission(
        title = title,
        abstract = abstract ?: "",
        type = type ?: "",
        submittedTo = submittedTo ?: ""
    )
