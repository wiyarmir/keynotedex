package es.guillermoorellana.keynotedex.backend

import com.nhaarman.mockitokotlin2.mock
import es.guillermoorellana.keynotedex.backend.auth.JwtConfig
import es.guillermoorellana.keynotedex.backend.auth.JwtTokenProvider
import es.guillermoorellana.keynotedex.backend.auth.createJwtConfig
import es.guillermoorellana.keynotedex.backend.auth.createJwtTokenProvider
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpHeaders
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.withTestApplication
import java.nio.file.Files

fun testApp(
    storage: KeynotedexStorage = testStorage(),
    jwtTokenProvider: JwtTokenProvider? = null,
    callback: TestApplicationEngine.() -> Unit
) {
    val tempPath = Files.createTempDirectory(null).toFile().apply { deleteOnExit() }
    try {
        withTestApplication(
            {
                (environment.config as MapApplicationConfig).apply {
                    put("jwt.domain", "domain")
                    put("jwt.audience", "audience")
                    put("jwt.realm", "realm")
                    put("jwt.secret", "secret")
                }
                _jwtConfig = createJwtConfig(environment)
                keynotedex(
                    storage = storage,
                    jwtConfig = _jwtConfig,
                    jwtTokenProvider = jwtTokenProvider ?: createJwtTokenProvider(_jwtConfig)
                )
            },
            {
                this.jwtConfig = jwtConfig
                callback()
            }
        )
    } finally {
        tempPath.deleteRecursively()
    }
}

lateinit var _jwtConfig: JwtConfig
var TestApplicationEngine.jwtConfig: JwtConfig
    get() = _jwtConfig
    set(value) {
        _jwtConfig = value
    }

fun TestApplicationRequest.addAuthHeader(jwtConfig: JwtConfig, userId: String) {
    addHeader(HttpHeaders.Authorization, "Bearer " + jwtConfig.makeUserToken(userId))
}

private fun testStorage(): KeynotedexStorage = mock { }
