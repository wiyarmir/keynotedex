package es.guillermoorellana.keynotedex.backend.data.sessions

import java.io.Closeable

interface SessionStorage : Closeable {
    fun getById(submissionId: Long): Session?
    fun all(): List<Session>
    fun allByUserId(userId: String): List<Session>
    fun create(session: Session)
    fun update(session: Session)
}
