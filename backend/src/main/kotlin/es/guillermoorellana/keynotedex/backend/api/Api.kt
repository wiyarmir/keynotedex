@file:UseExperimental(KtorExperimentalLocationsAPI::class)

package es.guillermoorellana.keynotedex.backend.api

import es.guillermoorellana.keynotedex.backend.api.conference.getConference
import es.guillermoorellana.keynotedex.backend.api.sessions.getSession
import es.guillermoorellana.keynotedex.backend.api.sessions.postSession
import es.guillermoorellana.keynotedex.backend.api.sessions.putSession
import es.guillermoorellana.keynotedex.backend.api.signin.postSignIn
import es.guillermoorellana.keynotedex.backend.api.signout.postSignOut
import es.guillermoorellana.keynotedex.backend.api.signup.postSignUp
import es.guillermoorellana.keynotedex.backend.api.user.getUser
import es.guillermoorellana.keynotedex.backend.api.user.putUser
import es.guillermoorellana.keynotedex.backend.auth.JwtTokenProvider
import es.guillermoorellana.keynotedex.backend.auth.UserPrincipal
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.data.sessions.Session
import es.guillermoorellana.keynotedex.backend.data.sessions.SessionStorage
import es.guillermoorellana.keynotedex.backend.data.sessions.toDto
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.datasource.responses.UserProfileResponse
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.util.pipeline.PipelineContext

fun Route.api(dao: KeynotedexStorage, jwtTokenProvider: JwtTokenProvider) {
    getConference(dao)

    getSession(dao, dao)
    postSession(dao, dao)
    putSession(dao, dao)

    getUser(dao, dao)
    putUser(dao, dao)

    postSignIn(dao, jwtTokenProvider)

    postSignOut()

    postSignUp(dao, jwtTokenProvider)
}

fun PipelineContext<Unit, ApplicationCall>.getCurrentLoggedUser(userStorage: UserStorage) =
    call.authentication.principal<UserPrincipal>()?.let { userStorage.retrieveUser(it.userId) }

suspend fun PipelineContext<Unit, ApplicationCall>.doUserProfileResponse(
    httpStatusCode: HttpStatusCode,
    user: User,
    sessionStorage: SessionStorage,
    currentUser: User?
) {
    val sessions = sessionStorage.allByUserId(user.userId)
        .filter { it.isPublic || currentUser?.userId == it.submitterId }

    call.respond(
        httpStatusCode,
        UserProfileResponse(
            user = user.toDto(),
            sessions = sessions.map(Session::toDto),
            editable = currentUser?.userId == user.userId
        )
    )
}

private val userIdPattern = "[a-zA-Z0-9_.]+".toRegex()
fun String.isValidUserId() = matches(userIdPattern)
