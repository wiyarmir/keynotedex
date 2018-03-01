package es.guillermoorellana.keynotedex.backend.dao.users

import org.jetbrains.squash.results.ResultRow
import org.jetbrains.squash.results.get

fun transformUser(it: ResultRow): User =
    User(
        it[UsersTable.id],
        it[UsersTable.email],
        it[UsersTable.displayName],
        it[UsersTable.passwordHash]
    )

fun User.toDto() =
    es.guillermoorellana.keynotedex.dto.User(
        userId = userId,
        displayName = displayName
    )
