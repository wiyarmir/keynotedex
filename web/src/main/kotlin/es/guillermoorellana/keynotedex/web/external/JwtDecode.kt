package es.guillermoorellana.keynotedex.web.external

@JsModule("jwt-decode")
external fun jwtDecode(rawToken: String): JwtParsedToken

external interface JwtParsedToken {
    val aud: String
    val exp: Long
    val iss: String
    val sub: String
}

fun JwtParsedToken.getClaim(key: String): String = asDynamic()[key].toString()
