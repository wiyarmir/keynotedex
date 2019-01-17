package es.guillermoorellana.keynotedex.backend.data.users

import java.io.Closeable

interface UserStorage : Closeable {
    fun createUser(user: User)
    fun retrieveUser(userId: String, hash: String? = null): User?
    fun retrieveUserByEmail(email: String): User?
    fun updateUser(user: User)
}
