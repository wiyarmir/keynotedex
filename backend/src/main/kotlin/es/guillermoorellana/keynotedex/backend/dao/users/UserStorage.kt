package es.guillermoorellana.keynotedex.backend.dao.users

import java.io.*

interface UserStorage : Closeable {
    fun createUser(user: User)
    fun retrieveUser(userId: String, hash: String? = null): User?
    fun retrieveUserByEmail(email: String): User?
    fun updateUser(user: User)
}
