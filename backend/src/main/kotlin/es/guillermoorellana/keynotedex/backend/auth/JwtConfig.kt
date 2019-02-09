package es.guillermoorellana.keynotedex.backend.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.ApplicationEnvironment
import io.ktor.auth.jwt.JWTAuthenticationProvider
import java.util.Date
import java.util.concurrent.TimeUnit

typealias JwtTokenProvider = (userId: String) -> String

fun createJwtConfig(environment: ApplicationEnvironment) = JwtConfig(environment = environment)

fun createJwtTokenProvider(jwtConfig: JwtConfig): JwtTokenProvider = { userId -> jwtConfig.makeUserToken(userId) }

enum class JwtAudience(val audienceName: String) {
    USER("user")
}

class JwtConfig(
    environment: ApplicationEnvironment,
    secret: String = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "deadbeef",
    private val realm: String = environment.config.property("jwt.realm").getString(),
    private val issuer: String = environment.config.property("jwt.domain").getString(),
    private val validityInMs: Long = TimeUnit.MILLISECONDS.convert(10, TimeUnit.HOURS),
    private val algorithm: Algorithm = Algorithm.HMAC512(secret)
) {

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    private val expiration
        get() = Date(System.currentTimeMillis() + validityInMs)

    fun makeUserToken(userId: String): String = JWT.create()
        .withSubject("$userId@keynotedex.local")
        .withIssuer(issuer)
        .withClaim("id", userId)
        .withExpiresAt(expiration)
        .withAudience(JwtAudience.USER.audienceName)
        .sign(algorithm)

    fun applyJwtConfig(config: JWTAuthenticationProvider) = config.run {
        realm = this@JwtConfig.realm
        verifier(verifier)
        validate { credential ->
            val supportedAudiences = JwtAudience.values().map { it.audienceName }
            if ((supportedAudiences intersect credential.payload.audience).isEmpty()) {
                return@validate null
            }
            val userId = credential.payload.getClaim("id").asString()
            UserPrincipal(userId)
        }
    }
}
