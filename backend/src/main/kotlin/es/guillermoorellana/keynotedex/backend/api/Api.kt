@file:UseExperimental(KtorExperimentalLocationsAPI::class)

package es.guillermoorellana.keynotedex.backend.api

import es.guillermoorellana.keynotedex.backend.Session
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.data.submissions.Submission
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.data.submissions.toDto
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.responses.UserProfileResponse
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.util.pipeline.PipelineContext

fun Route.api(dao: KeynotedexStorage) {
    GetConference(dao)
    GetLogin(dao)
    GetSubmission(dao, dao)
    GetUser(dao, dao)

    PostSignIn(dao)
    PostSignOut()
    PostSignUp(dao)

    PutUser(dao, dao)
}

fun PipelineContext<Unit, ApplicationCall>.getCurrentLoggedUser(userStorage: UserStorage) =
    call.sessions.get<Session>()?.let { userStorage.retrieveUser(it.userId) }

suspend fun PipelineContext<Unit, ApplicationCall>.doUserProfileResponse(
    user: User,
    submissionStorage: SubmissionStorage,
    currentUser: User?
) {
    val submissions = submissionStorage.submissionsByUserId(user.userId)
        .filter { it.isPublic || currentUser?.userId == it.submitterId }

    call.respond(
        UserProfileResponse(
            user = user.toDto(),
            submissions = submissions.map(Submission::toDto),
            editable = currentUser?.userId == user.userId
        )
    )
}

private val userIdPattern = "[a-zA-Z0-9_.]+".toRegex()
fun String.isValidUserId() = matches(userIdPattern)
