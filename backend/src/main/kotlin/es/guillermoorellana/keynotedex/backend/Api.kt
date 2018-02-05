package es.guillermoorellana.keynotedex.backend

import es.guillermoorellana.keynotedex.backend.dao.*
import es.guillermoorellana.keynotedex.backend.dao.conferences.*
import es.guillermoorellana.keynotedex.backend.dao.submissions.*
import es.guillermoorellana.keynotedex.backend.dao.users.*
import es.guillermoorellana.keynotedex.backend.submission.*
import es.guillermoorellana.keynotedex.backend.user.*
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
    apiGetSubmission(dao)
    apiGetUser(dao)

    apiPostLogin(dao)
    apiPostRegister(dao, hashFunction)
}

private fun Route.apiGetConference(storage: KeynotedexStorage) {
    accept(ContentType.Application.Json) {
        get<ConferencePage> {
            val conference = storage.conference(it.conferenceId)
            when (conference) {
                null -> call.respond(ErrorResponse("Can't find conference with id ${it.conferenceId}"))
                else -> call.respond(ConferenceResponse(conference.toDto()))
            }
        }
    }
}

private fun Route.apiGetLogin(dao: UserStorage) {
    accept(ContentType.Application.Json) {
        get<LoginPage> {
            val user = call.sessions.get<Session>()?.let { dao.user(it.userId) }
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

private fun Route.apiGetSubmission(dao: SubmissionStorage) {
    accept(ContentType.Application.Json) {
        get<SubmissionPage> {
            when {
                it.submissionId == null || it.submissionId.isNullOrBlank() -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("Bad request")
                    )
                }
                else -> {
                    val submission = dao.submissionById(it.submissionId)
                    when (submission) {
                        null -> call.respond(
                            HttpStatusCode.NotFound,
                            ErrorResponse("Not found")
                        )
                        else -> call.respond(SubmissionResponse(submission.toDto()))
                    }
                }
            }
        }
    }
}

private fun Route.apiGetUser(dao: KeynotedexStorage) {
    accept(ContentType.Application.Json) {
        get<UserPage> {
            val user = dao.user(it.userId)
            when (user) {
                null -> call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("User ${it.userId} doesn't exist")
                )
                else -> {
                    val submissions = dao.submissionsByUserId(user.userId)
                    call.respond(UserResponse(user.copy(submissions = submissions).toDto()))
                }
            }
        }
    }
}

private fun Route.apiPostLogin(dao: UserStorage) {
    accept(ContentType.Application.Json) {
        post<LoginPage> {
            val params = call.receiveParameters()
            val userId = params["userId"] ?: ""
            val password = params["password"] ?: ""

            val login = when {
                userId.length < 4 -> null
                password.length < 6 -> null
                !userNameValid(userId) -> null
                else -> {
                    dao.user(userId, hash(password))
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

private fun Route.apiPostRegister(dao: UserStorage, hashFunction: (String) -> String) {

    fun passwordNotValid(password: String) = password.length < 6
    fun userIdShort(userId: String) = userId.length < 4
    fun userNameNotValid(userName: String) = !userNameValid(userName)
    fun userInDatabase(dao: UserStorage, userId: String) = dao.user(userId) != null

    post<RegisterPage> {
        val user = call.sessions.get<Session>()?.let { dao.user(it.userId) }
        if (user != null) {
            val dtoUser = user.toDto()
            call.redirect(UserResponse(dtoUser))
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
            dao.createUser(newUser)
        } catch (e: Throwable) {
            application.environment.log.error("Failed to register user", e)
            when {
                userInDatabase(dao, userId) -> call.respond(
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
        call.respond(UserResponse(newUser.toDto()))
    }
}
