package es.guillermoorellana.keynotedex.backend

import com.nhaarman.mockitokotlin2.mock
import es.guillermoorellana.keynotedex.backend.auth.JwtConfig
import es.guillermoorellana.keynotedex.backend.auth.JwtTokenProvider
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
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("jwt.domain", "domain")
                put("jwt.audience", "audience")
                put("jwt.realm", "realm")
                put("jwt.secret", "secret")
            }
            keynotedex(
                storage = storage,
                jwtTokenProvider = jwtTokenProvider ?: createJwtTokenProvider(createJwtConfig())
            )
        }, callback)
    } finally {
        tempPath.deleteRecursively()
    }
}

fun TestApplicationRequest.addAuthHeader(jwtConfig: JwtConfig, userId: String) {
    addHeader(HttpHeaders.Authorization, "Bearer " + jwtConfig.makeToken(userId))
}

private fun testStorage(): KeynotedexStorage = mock { }
