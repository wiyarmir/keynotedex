package es.guillermoorellana.keynotedex.backend.api.signout

import es.guillermoorellana.keynotedex.backend.testApp
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PostSignOutTest {

    private val TestApplicationEngine.endpoint
        get() = application.locations.href(SignOutEndpoint())

    @Test
    fun `when requesting signout then it returns ok`() = testApp {
        handleRequest(HttpMethod.Post, endpoint).apply {
            assertThat(response.status(), equalTo(HttpStatusCode.OK))
        }
    }

    @Test
    fun `when requesting signout then session cookie is null`() = testApp {
        handleRequest(HttpMethod.Post, endpoint).apply {
            assertThat(response.cookies["SESSION"], nullValue())
        }
    }
}
