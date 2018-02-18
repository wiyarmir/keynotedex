package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.dao.*
import es.guillermoorellana.keynotedex.backend.dao.conferences.*
import es.guillermoorellana.keynotedex.backend.dao.submissions.*
import es.guillermoorellana.keynotedex.backend.dao.users.*
import es.guillermoorellana.keynotedex.responses.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.api(dao: KeynotedexStorage, hashFunction: (String) -> String) {
    apiGetConference(dao)
    apiGetLogin(dao)
    apiGetSubmission(dao, dao)
    apiGetUser(dao, dao)

    apiPostLogin(dao)
    apiPostRegister(dao, hashFunction)
}

private fun Route.apiGetConference(conferenceStorage: ConferenceStorage) {
    accept(ContentType.Application.Json) {
        get<ConferenceEndpoint> {
            val conference = conferenceStorage.conference(it.conferenceId)
            when (conference) {
                null -> call.respond(ErrorResponse("Can't find conference with id ${it.conferenceId}"))
                else -> call.respond(ConferenceResponse(conference.toDto()))
            }
        }
    }
}

private fun Route.apiGetLogin(userStorage: UserStorage) {
    accept(ContentType.Application.Json) {
        get<LoginEndpoint> {
            val user = call.sessions.get<Session>()?.let { userStorage.user(it.userId) }
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
        get<SubmissionEndpoint> {
            val submission = submissionStorage.submissionById(it.submissionId)
            if (submission == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Not found"))
                return@get
            }
            if (submission.isPublic) {
                call.respond(SubmissionResponse(submission.toDto()))
                return@get
            }
            val user = call.sessions.get<Session>()?.let { userStorage.user(it.userId) }
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
            val user = userStorage.user(it.userId)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("User ${it.userId} doesn't exist"))
                return@get
            }

            val currentUser = call.sessions.get<Session>()?.let { userStorage.user(it.userId) }
            val submissions = submissionStorage.submissionsByUserId(user.userId)
                .filter { it.isPublic || currentUser?.userId == it.submitterId }

            call.respond(UserProfileResponse(user.toDto(), submissions.map(Submission::toDto)))
        }
    }
}

private fun Route.apiPostLogin(userStorage: UserStorage) {
    accept(ContentType.Application.Json) {
        post<LoginEndpoint> {
            val params = call.receiveParameters()
            val userId = params["userId"] ?: ""
            val password = params["password"] ?: ""

            val login = when {
                userId.length < 4 -> null
                password.length < 6 -> null
                !userNameValid(userId) -> null
                else -> {
                    userStorage.user(userId, hash(password))
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

private fun Route.apiPostRegister(userStorage: UserStorage, hashFunction: (String) -> String) {

    fun passwordNotValid(password: String) = password.length < 6
    fun userIdShort(userId: String) = userId.length < 4
    fun userNameNotValid(userName: String) = !userNameValid(userName)
    fun userInDatabase(dao: UserStorage, userId: String) = dao.user(userId) != null

    post<RegisterEndpoint> {
        val user = call.sessions.get<Session>()?.let { userStorage.user(it.userId) }
        if (user != null) {
            val dtoUser = user.toDto()
            call.redirect(UserProfileResponse(dtoUser))
            return@post
        }

        val userId = it.userId
        val password = it.password

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
                    ErrorResponse(message = "Login should be consists of digits, letters, dots or underscores")
                )
                return@post
            }
        }

        val hash = hashFunction(password)
        val newUser = User(userId = userId, passwordHash = hash)

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
