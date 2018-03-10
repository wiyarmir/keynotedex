package es.guillermoorellana.keynotedex.backend.dao

import es.guillermoorellana.keynotedex.backend.dao.conferences.ConferencesTable
import es.guillermoorellana.keynotedex.backend.dao.conferences.transformConference
import es.guillermoorellana.keynotedex.backend.dao.submissions.Submission
import es.guillermoorellana.keynotedex.backend.dao.submissions.SubmissionsTable
import es.guillermoorellana.keynotedex.backend.dao.submissions.transformSubmission
import es.guillermoorellana.keynotedex.backend.dao.talks.TalksTable
import es.guillermoorellana.keynotedex.backend.dao.users.User
import es.guillermoorellana.keynotedex.backend.dao.users.UsersTable
import es.guillermoorellana.keynotedex.backend.dao.users.transformUser
import org.jetbrains.squash.connection.DatabaseConnection
import org.jetbrains.squash.connection.transaction
import org.jetbrains.squash.dialects.h2.H2Connection
import org.jetbrains.squash.expressions.eq
import org.jetbrains.squash.query.from
import org.jetbrains.squash.query.where
import org.jetbrains.squash.results.get
import org.jetbrains.squash.schema.create
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.set
import org.jetbrains.squash.statements.update
import org.jetbrains.squash.statements.values
import java.io.File

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
                it[bio] = user.bio
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
