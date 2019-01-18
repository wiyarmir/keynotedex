package es.guillermoorellana.keynotedex.backend.data

import es.guillermoorellana.keynotedex.backend.data.conferences.ConferencesTable
import es.guillermoorellana.keynotedex.backend.data.conferences.transformConference
import es.guillermoorellana.keynotedex.backend.data.submissions.Submission
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionsTable
import es.guillermoorellana.keynotedex.backend.data.submissions.transformSubmission
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.data.users.UsersTable
import es.guillermoorellana.keynotedex.backend.data.users.transformUser
import org.hashids.Hashids
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

val hashids = Hashids(salt = "KeynotedexIsGreat", length = 10)

class KeynotedexDatabase(val db: DatabaseConnection = H2Connection.createMemoryConnection()) : KeynotedexStorage {
    constructor(dir: File) : this(H2Connection.create("jdbc:h2:file:${dir.canonicalFile.absolutePath}"))

    init {
        db.transaction {
            databaseSchema().create(
                ConferencesTable,
                UsersTable,
                SubmissionsTable
            )
        }
    }

    override fun retrieveUser(userId: String, hash: String?) = db.transaction {
        from(UsersTable)
            .where { UsersTable.id eq userId }
            .execute()
            .mapNotNull { row ->
                if (hash == null || row[UsersTable.passwordHash] == hash) {
                    transformUser(row)
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
            .values { values ->
                values[id] = user.userId
                values[displayName] = user.displayName
                values[email] = user.email
                values[passwordHash] = user.passwordHash
            }
            .execute()
    }

    override fun updateUser(user: User) = db.transaction {
        update(UsersTable)
            .set { statement ->
                statement[displayName] = user.displayName
                statement[bio] = user.bio
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
            .where { SubmissionsTable.id eq hashids.decode(submissionId).first() }
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

    override fun create(submission: Submission) = db.transaction {
        insertInto(SubmissionsTable)
            .values { values ->
                values[submitter] = submission.submitterId
                values[title] = submission.title
                values[abstract] = submission.abstract
                values[public] = submission.isPublic
            }
            .execute()
    }

    override fun close() {
    }
}
