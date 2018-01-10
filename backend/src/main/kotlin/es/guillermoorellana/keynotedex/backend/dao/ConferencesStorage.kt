package es.guillermoorellana.keynotedex.backend.dao

import es.guillermoorellana.keynotedex.backend.conference.Conference
import es.guillermoorellana.keynotedex.backend.user.model.User
import java.io.Closeable

interface ConferencesStorage : Closeable {
    fun user(userId: String, hash: String? = null): User?
    fun userByEmail(email: String): User?
    fun createUser(user: User)

    fun conferences(): List<Conference>
}


