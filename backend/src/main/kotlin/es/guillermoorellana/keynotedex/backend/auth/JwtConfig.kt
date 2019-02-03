package es.guillermoorellana.keynotedex.backend.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.ApplicationEnvironment
import io.ktor.auth.jwt.JWTAuthenticationProvider
import java.util.Date
import java.util.concurrent.TimeUnit

typealias JwtTokenProvider = (String) -> String

class JwtConfig(
    environment: ApplicationEnvironment,
    secret: String = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "deadbeef",
    val audience: String = environment.config.property("jwt.audience").getString(),
    val realm: String = environment.config.property("jwt.realm").getString(),
    private val issuer: String = environment.config.property("jwt.domain").getString(),
    private val validityInMs: Long = TimeUnit.MILLISECONDS.convert(10, TimeUnit.HOURS),
    private val algorithm: Algorithm = Algorithm.HMAC512(secret)
) {

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    fun makeToken(userId: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", userId)
        .withExpiresAt(getExpiration())
        .withAudience(audience)
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

    fun applyJwtConfig(config: JWTAuthenticationProvider) = config.run {
        realm = this@JwtConfig.realm
        verifier(verifier)
        validate { credential ->
            if (credential.payload.audience.contains(audience))
                UserPrincipal(credential.payload.getClaim("id").asString())
            else null
        }
    }
}
