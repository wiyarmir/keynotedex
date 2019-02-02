@file:Suppress("EXPERIMENTAL_API_USAGE")

package es.guillermoorellana.keynotedex.backend.api.user

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.backend.testApp
import es.guillermoorellana.keynotedex.responses.UserProfileResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import kotlinx.serialization.json.JSON
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Test

class GetUserTest {

    private val testId = "testId"
    private val testUser = User(testId)
    private val validUserStorage: KeynotedexStorage = mock {
        on { retrieveUser(eq(testId), anyOrNull()) } doReturn testUser
    }

    private fun TestApplicationEngine.endpoint(userId: String) = application.locations.href(UserEndpoint(userId))

    @Test
    fun `when requesting with no headers and no userId then it returns html`() = testApp {
        handleRequest(HttpMethod.Get, endpoint("")).apply {
            assertThat(response.contentType(), equalTo(ContentType.Text.Html.withCharset(Charsets.UTF_8)))
        }
    }

    @Test
    @Ignore("It's actually unhandled, the framework is the one doing the 404")
    fun `when requesting with headers and no userId then it returns not found`() = testApp {
        handleRequest(HttpMethod.Get, endpoint("")) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.NotFound))
        }
    }

    @Test
    fun `when requesting with headers and invalid userId then it returns not found`() = testApp {
        handleRequest(HttpMethod.Get, endpoint(testId)) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.NotFound))
        }
    }

    @Test
    fun `when requesting with headers and valid userId then it returns ok`() = testApp(validUserStorage) {
        handleRequest(HttpMethod.Get, endpoint(testId)) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.OK))
        }
    }

    @Test
    fun `when requesting with headers and valid userId then it returns such user`() = testApp(validUserStorage) {
        handleRequest(HttpMethod.Get, endpoint(testId)) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
        }.apply {
            assertThat(JSON.parse(UserProfileResponse.serializer(), response.content!!).user, equalTo(testUser.toDto()))
        }
    }
}
