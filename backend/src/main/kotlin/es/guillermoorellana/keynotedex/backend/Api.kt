@file:UseExperimental(KtorExperimentalLocationsAPI::class)

package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.data.conferences.ConferenceStorage
import es.guillermoorellana.keynotedex.backend.data.conferences.toDto
import es.guillermoorellana.keynotedex.backend.data.submissions.Submission
import es.guillermoorellana.keynotedex.backend.data.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.data.submissions.toDto
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.data.users.UserStorage
import es.guillermoorellana.keynotedex.backend.data.users.toDao
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.responses.ConferenceResponse
import es.guillermoorellana.keynotedex.responses.ErrorResponse
import es.guillermoorellana.keynotedex.responses.LoginResponse
import es.guillermoorellana.keynotedex.responses.LogoutResponse
import es.guillermoorellana.keynotedex.responses.SubmissionResponse
import es.guillermoorellana.keynotedex.responses.UserProfileResponse
import io.ktor.application.ApplicationCall
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import io.ktor.routing.contentType
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.pipeline.PipelineContext

fun Route.api(dao: KeynotedexStorage) {
    apiGetConference(dao)
    apiGetLogin(dao)
    apiGetSubmission(dao, dao)
    apiGetUser(dao, dao)

    apiPostSignIn(dao)
    apiPostSignOut()
    apiPostSignUp(dao)

    apiPutUser(dao, dao)
}

private fun Route.apiGetConference(conferenceStorage: ConferenceStorage) {
    accept(ContentType.Application.Json) {
        get<ConferenceEndpoint> { endpoint ->
            endpoint.conferenceId?.let { conferenceId ->
                val conference = conferenceStorage.conference(conferenceId)
                when (conference) {
                    null -> call.respond(ErrorResponse("Can't find conference with id ${endpoint.conferenceId}"))
                    else -> call.respond(ConferenceResponse(conference.toDto()))
                }
            }
        }
    }
}

private fun Route.apiGetLogin(userStorage: UserStorage) {
    accept(ContentType.Application.Json) {
        get<LoginEndpoint> {
            val user = getCurrentLoggedUser(userStorage)
            when (user) {
                null -> call.respond(
                    HttpStatusCode.Forbidden,
                    ErrorResponse("Forbidden")
                )
                else -> call.respond(LoginResponse(user.toDto()))
            }
        }
    }
}

private fun Route.apiGetSubmission(submissionStorage: SubmissionStorage, userStorage: UserStorage) {
    accept(ContentType.Application.Json) {
        get<SubmissionsEndpoint> { endpoint ->
            val submission = submissionStorage.submissionById(endpoint.submissionId)
            if (submission == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
                return@get
            }
            if (submission.isPublic) {
                call.respond(SubmissionResponse(submission.toDto()))
                return@get
            }
            val user = getCurrentLoggedUser(userStorage)
            if (user?.userId == submission.submitterId) {
                call.respond(SubmissionResponse(submission.toDto()))
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
            }
        }
    }
}

private fun Route.apiGetUser(userStorage: UserStorage, submissionStorage: SubmissionStorage) {
    accept(ContentType.Application.Json) {
        get<UserEndpoint> {
            val userId = it.userId
            val user = userStorage.retrieveUser(userId)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("User $userId doesn't exist"))
                return@get
            }
            val currentUser = getCurrentLoggedUser(userStorage)
            doUserProfileResponse(user, submissionStorage, currentUser)
        }
    }
}

private fun Route.apiPutUser(userStorage: UserStorage, submissionStorage: SubmissionStorage) {
    accept(ContentType.Application.Json) {
        contentType(ContentType.Application.Json) {
            put<UserEndpoint> {
                val request: UserProfileUpdateRequest = call.receive()
                val sessionUser = getCurrentLoggedUser(userStorage)
                if (request.user.userId != sessionUser?.userId) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse("Nope"))
                }
                userStorage.updateUser(request.user.toDao())
                val updatedUser = userStorage.retrieveUser(request.user.userId)!!
                doUserProfileResponse(updatedUser, submissionStorage, sessionUser)
                call.respond(HttpStatusCode.Accepted, UserProfileResponse(updatedUser.toDto()))
            }
        }
    }
}

private fun Route.apiPostSignIn(userStorage: UserStorage) {
    accept(ContentType.Application.Json) {
        post<LoginEndpoint> {
            val params = call.receiveParameters()
            val userId = params["userId"] ?: ""
            val password = params["password"] ?: ""

            val login = when {
                userId.length < 4 -> null
                password.length < 6 -> null
                !userId.isValidUserId() -> null
                else -> {
                    userStorage.retrieveUser(userId, application.hashPassword(password))
                }
            }

            when (login) {
                null -> call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(message = "Invalid username or password")
                )
                else -> {
                    call.sessions.set(Session(login.userId))
                    call.respond(LoginResponse(login.toDto()))
                }
            }
        }
    }
}

private fun Route.apiPostSignOut() {
    accept(ContentType.Application.Json) {
        post<LogoutEndpoint> {
            call.sessions.set<Session>(null)
            call.respond(LogoutResponse())
        }
    }
}

private fun Route.apiPostSignUp(userStorage: UserStorage) {

    fun passwordNotValid(password: String?) = password?.let { it.length < 6 } ?: true
    fun userIdShort(userId: String?) = userId?.let { it.length < 4 } ?: true
    fun userNameNotValid(userName: String?) = userName?.isValidUserId()?.not() ?: true
    fun userInDatabase(dao: UserStorage, userId: String?) = userId?.let { dao.retrieveUser(it) != null } ?: false

    post<RegisterEndpoint> {
        val user = getCurrentLoggedUser(userStorage)
        if (user != null) {
            call.respond(
                HttpStatusCode.MethodNotAllowed,
                ErrorResponse(message = "You are already logged in")
            )
            return@post
        }

        val params = call.receive<Parameters>()
        val userId = params["userId"]
        val password = params["password"]
        val email = params["email"]
        val displayName = params["displayName"]

        when {
            passwordNotValid(password) -> {
                call.respond(
                    HttpStatusCode.PreconditionFailed,
                    ErrorResponse(message = "Password should be at least 6 characters long")
                )
                return@post
            }
            userIdShort(userId) -> {
                call.respond(
                    HttpStatusCode.PreconditionFailed,
                    ErrorResponse(message = "Login should be at least 4 characters long")
                )
                return@post
            }
            userNameNotValid(userId) -> {
                call.respond(
                    HttpStatusCode.PreconditionFailed,
                    ErrorResponse(message = "Login should consist of digits, letters, dots or underscores")
                )
                return@post
            }
        }

        val hash = application.hashPassword(requireNotNull(password))
        val newUser =
            User(userId = requireNotNull(userId), passwordHash = hash, displayName = displayName, email = email)

        try {
            userStorage.createUser(newUser)
        } catch (e: Throwable) {
            application.environment.log.error("Failed to register user", e)
            when {
                userInDatabase(userStorage, userId) -> call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse(message = "User with the following login is already registered")
                )
                else -> {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse(message = "Failed to register")
                    )
                }
            }
            return@post
        }

        call.sessions.set(Session(newUser.userId))
        call.respond(UserProfileResponse(newUser.toDto()))
    }
}

private fun PipelineContext<Unit, ApplicationCall>.getCurrentLoggedUser(userStorage: UserStorage) =
    call.sessions.get<Session>()?.let { userStorage.retrieveUser(it.userId) }

private suspend fun PipelineContext<Unit, ApplicationCall>.doUserProfileResponse(
    user: User,
    submissionStorage: SubmissionStorage,
    currentUser: User?
) {
    val submissions =
        submissionStorage.submissionsByUserId(user.userId)
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
private fun String.isValidUserId() = matches(userIdPattern)
