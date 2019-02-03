@file:UseExperimental(KtorExperimentalLocationsAPI::class)

package es.guillermoorellana.keynotedex.backend.api

import es.guillermoorellana.keynotedex.backend.api.conference.GetConference
import es.guillermoorellana.keynotedex.backend.api.signin.PostSignIn
import es.guillermoorellana.keynotedex.backend.api.signout.PostSignOut
import es.guillermoorellana.keynotedex.backend.api.signup.PostSignUp
import es.guillermoorellana.keynotedex.backend.api.submission.GetSubmission
import es.guillermoorellana.keynotedex.backend.api.submission.PostSubmission
import es.guillermoorellana.keynotedex.backend.api.submission.PutSubmission
import es.guillermoorellana.keynotedex.backend.api.user.GetUser
import es.guillermoorellana.keynotedex.backend.api.user.PutUser
import es.guillermoorellana.keynotedex.backend.auth.JwtTokenProvider
import es.guillermoorellana.keynotedex.backend.auth.UserPrincipal
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
import io.ktor.auth.authentication
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.util.pipeline.PipelineContext

fun Route.api(dao: KeynotedexStorage, jwtTokenProvider: JwtTokenProvider) {
    GetConference(dao)

    GetSubmission(dao, dao)
    PostSubmission(dao, dao)
    PutSubmission(dao, dao)

    GetUser(dao, dao)
    PutUser(dao, dao)

    PostSignIn(dao, jwtTokenProvider)

    PostSignOut()

    PostSignUp(dao, jwtTokenProvider)
}

fun PipelineContext<Unit, ApplicationCall>.getCurrentLoggedUser(userStorage: UserStorage) =
    call.authentication.principal<UserPrincipal>()?.let { userStorage.retrieveUser(it.userId) }

suspend fun PipelineContext<Unit, ApplicationCall>.doUserProfileResponse(
    user: User,
    submissionStorage: SubmissionStorage,
    currentUser: User?
) {
    val submissions = submissionStorage.allByUserId(user.userId)
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
