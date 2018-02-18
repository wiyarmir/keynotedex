package es.guillermoorellana.keynotedex.backend.dao.users

import java.io.*

interface UserStorage : Closeable {
    fun user(userId: String, hash: String? = null): User?
    fun userByEmail(email: String): User?
    fun createUser(user: User)
}
