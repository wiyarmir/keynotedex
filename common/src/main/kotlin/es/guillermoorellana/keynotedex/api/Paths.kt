package es.guillermoorellana.keynotedex.api

object Api {
    const val loc = "/api"

    object V1 {
        const val loc = "${Api.loc}/v1"

        object Paths {
            const val user = "${V1.loc}/users/{userId}"
            const val submissions = "${V1.loc}/submissions/{submissionId}"
            const val conferences = "${V1.loc}/conferences/{conferenceId}"
            const val register = "${V1.loc}/register"
            const val login = "${V1.loc}/login"
            const val logout = "${V1.loc}/logout"
        }
    }
}
