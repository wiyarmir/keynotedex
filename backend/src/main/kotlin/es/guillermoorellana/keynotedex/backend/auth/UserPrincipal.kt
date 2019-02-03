package es.guillermoorellana.keynotedex.backend.auth

import io.ktor.auth.Principal

inline class UserPrincipal(val userId: String) : Principal
