package es.guillermoorellana.keynotedex.backend.data.submissions

import java.io.Closeable

interface SubmissionStorage : Closeable {
    fun getById(submissionId: Long): Submission?
    fun all(): List<Submission>
    fun allByUserId(userId: String): List<Submission>
    fun create(submission: Submission)
    fun update(submission: Submission)
}
