package es.guillermoorellana.keynotedex.backend.dao

import es.guillermoorellana.keynotedex.backend.conferences.Conference
import es.guillermoorellana.keynotedex.backend.user.model.User
import java.io.Closeable

interface UserStorage : Closeable {
    fun user(userId: String, hash: String? = null): User?
    fun userByEmail(email: String): User?
    fun createUser(user: User)
}

interface ConferenceStorage : Closeable {
    fun conference(conferenceId: String): Conference?
    fun conferences(): List<Conference>
}

interface KeynotedexStorage :
        Closeable,
        UserStorage,
        ConferenceStorage


