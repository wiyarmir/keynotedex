package es.guillermoorellana.keynotedex.api

object ApiPaths {
    const val user = "/{userId}"
    const val submissions = "/{userId}/{submissionId}"
    const val conferences = "/conferences/{conferenceId?}"
    const val register = "/register"
    const val login = "/login"
    const val logout = "/logout"
}
