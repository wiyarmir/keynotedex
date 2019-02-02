package es.guillermoorellana.keynotedex.backend

import com.nhaarman.mockitokotlin2.mock
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import io.ktor.config.MapApplicationConfig
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import java.nio.file.Files

fun testApp(storage: KeynotedexStorage = testStorage(), callback: TestApplicationEngine.() -> Unit): Unit {

    val tempPath = Files.createTempDirectory(null).toFile().apply { deleteOnExit() }
    try {
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                // put("youkube.upload.dir", tempPath.absolutePath)
            }
            keynotedex(storage = storage)
        }, callback)
    } finally {
        tempPath.deleteRecursively()
    }
}

private fun testStorage(): KeynotedexStorage = mock { }
