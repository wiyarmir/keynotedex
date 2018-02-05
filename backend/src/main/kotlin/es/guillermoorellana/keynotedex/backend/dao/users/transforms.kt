package es.guillermoorellana.keynotedex.backend.dao.users

import es.guillermoorellana.keynotedex.backend.dao.submissions.*
import org.jetbrains.squash.results.*

fun transformUser(it: ResultRow): User =
    User(
        it[Users.id],
        it[Users.email],
        it[Users.displayName],
        it[Users.passwordHash]
    )

fun User.toDto() =
    es.guillermoorellana.keynotedex.dto.User(
        userId = userId,
        displayName = displayName,
        submissions = submissions.map { it.toDto() }
    )
