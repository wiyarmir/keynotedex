package es.guillermoorellana.keynotedex.backend.data.submissions

import java.io.*

interface SubmissionStorage : Closeable {
    fun submissionById(submissionId: String): Submission?
    fun submissionsByUserId(userId: String): List<Submission>
    fun submissions(): List<Submission>
}
