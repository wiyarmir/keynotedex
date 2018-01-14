package es.guillermoorellana.keynotedex.backend.dao

import es.guillermoorellana.keynotedex.backend.conferences.Conference
import es.guillermoorellana.keynotedex.backend.dao.tables.Conferences
import es.guillermoorellana.keynotedex.backend.dao.tables.Talks
import es.guillermoorellana.keynotedex.backend.dao.tables.Users
import es.guillermoorellana.keynotedex.backend.user.model.User
import org.jetbrains.squash.connection.DatabaseConnection
import org.jetbrains.squash.connection.transaction
import org.jetbrains.squash.dialects.h2.H2Connection
import org.jetbrains.squash.expressions.eq
import org.jetbrains.squash.query.from
import org.jetbrains.squash.query.where
import org.jetbrains.squash.results.get
import org.jetbrains.squash.schema.create
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.values
import java.io.File

class KeynotedexDatabase(val db: DatabaseConnection = H2Connection.createMemoryConnection()) : KeynotedexStorage {
    constructor(dir: File) : this(H2Connection.create("jdbc:h2:file:${dir.canonicalFile.absolutePath}"))

    init {
        db.transaction { databaseSchema().create(Conferences, Talks, Users) }
    }

    override fun user(userId: String, hash: String?) = db.transaction {
        from(Users)
                .where { Users.id eq userId }
                .execute()
                .mapNotNull {
                    if (hash == null || it[Users.passwordHash] == hash) {
                        User(
                                userId,
                                it[Users.email],
                                it[Users.displayName],
                                it[Users.passwordHash]
                        )
                    } else {
                        null
                    }
                }
                .singleOrNull()
    }

    override fun userByEmail(email: String) = db.transaction {
        from(Users)
                .where { Users.email eq email }
                .execute()
                .map {
                    User(
                            it[Users.id],
                            email,
                            it[Users.displayName],
                            it[Users.passwordHash]
                    )
                }
                .singleOrNull()
    }

    override fun createUser(user: User) = db.transaction {
        insertInto(Users)
                .values {
                    it[id] = user.userId
                    it[displayName] = user.displayName
                    it[email] = user.email
                    it[passwordHash] = user.passwordHash
                }
                .execute()
    }

    override fun conferences() = db.transaction {
        from(Conferences)
                .execute()
                .map {
                    Conference(name = it[Conferences.name])
                }
                .toList()
    }

    override fun conference(conferenceId: String) = db.transaction {
        from(Conferences)
                .where { Conferences.id eq conferenceId }
                .execute()
                .map {
                    Conference(name = it[Conferences.name])
                }
                .singleOrNull()
    }

    override fun close() {
    }
}
