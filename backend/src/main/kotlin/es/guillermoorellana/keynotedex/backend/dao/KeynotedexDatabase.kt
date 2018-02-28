package es.guillermoorellana.keynotedex.backend.dao

import es.guillermoorellana.keynotedex.backend.dao.conferences.*
import es.guillermoorellana.keynotedex.backend.dao.submissions.*
import es.guillermoorellana.keynotedex.backend.dao.talks.*
import es.guillermoorellana.keynotedex.backend.dao.users.*
import org.jetbrains.squash.connection.*
import org.jetbrains.squash.dialects.h2.*
import org.jetbrains.squash.expressions.*
import org.jetbrains.squash.query.*
import org.jetbrains.squash.results.*
import org.jetbrains.squash.schema.*
import org.jetbrains.squash.statements.*
import java.io.*

class KeynotedexDatabase(val db: DatabaseConnection = H2Connection.createMemoryConnection()) : KeynotedexStorage {
    constructor(dir: File) : this(H2Connection.create("jdbc:h2:file:${dir.canonicalFile.absolutePath}"))

    init {
        db.transaction {
            databaseSchema().create(
                ConferencesTable,
                TalksTable,
                UsersTable,
                SubmissionsTable
            )
        }
    }

    override fun retrieveUser(userId: String, hash: String?) = db.transaction {
        from(UsersTable)
            .where { UsersTable.id eq userId }
            .execute()
            .mapNotNull {
                if (hash == null || it[UsersTable.passwordHash] == hash) {
                    transformUser(it)
                } else {
                    null
                }
            }
            .singleOrNull()
    }

    override fun retrieveUserByEmail(email: String) = db.transaction {
        from(UsersTable)
            .where { UsersTable.email eq email }
            .execute()
            .map(::transformUser)
            .singleOrNull()
    }

    override fun createUser(user: User) = db.transaction {
        insertInto(UsersTable)
            .values {
                it[id] = user.userId
                it[displayName] = user.displayName
                it[email] = user.email
                it[passwordHash] = user.passwordHash
            }
            .execute()
    }

    override fun updateUser(user: User) = db.transaction {
        update(UsersTable)
            .set {
                it[displayName] = user.displayName
            }
            .where { UsersTable.id eq user.userId }
            .execute()
    }

    override fun conferences() = db.transaction {
        from(ConferencesTable)
            .execute()
            .map(::transformConference)
            .toList()
    }

    override fun conference(conferenceId: String) = db.transaction {
        from(ConferencesTable)
            .where { ConferencesTable.id eq conferenceId }
            .execute()
            .map(::transformConference)
            .singleOrNull()
    }

    override fun submissionById(submissionId: String): Submission? = db.transaction {
        from(SubmissionsTable)
            .where { SubmissionsTable.id eq submissionId }
            .execute()
            .map(::transformSubmission)
            .singleOrNull()
    }

    override fun submissionsByUserId(userId: String): List<Submission> = db.transaction {
        from(SubmissionsTable)
            .where { SubmissionsTable.submitter eq userId }
            .execute()
            .map(::transformSubmission)
            .toList()
    }

    override fun submissions(): List<Submission> = db.transaction {
        from(ConferencesTable)
            .execute()
            .map(::transformSubmission)
            .toList()
    }

    override fun close() {
    }
}
