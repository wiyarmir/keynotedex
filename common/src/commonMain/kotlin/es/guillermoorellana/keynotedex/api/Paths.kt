package es.guillermoorellana.keynotedex.api

object Api {
    const val loc = "/api"

    object V1 {
        const val loc = "${Api.loc}/v1"

        object Paths {
            const val user = "$loc/users/{userId}"
            const val sessions = "$loc/sessions/{sessionId?}"
            const val conferences = "$loc/conferences/{conferenceId?}"
            const val signUp = "$loc/register"
            const val signIn = "$loc/login"
            const val signOut = "$loc/logout"
        }
    }
}
