package es.guillermoorellana.keynotedex.backend.api.user

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import es.guillermoorellana.keynotedex.backend.addAuthHeader
import es.guillermoorellana.keynotedex.backend.auth.JwtConfig
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.data.users.toDto
import es.guillermoorellana.keynotedex.backend.testApp
import es.guillermoorellana.keynotedex.datasource.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.datasource.responses.UserProfileResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlinx.serialization.json.JSON
import kotlinx.serialization.json.Json
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Test

class PutUserTest {
    private val testId = "testId"
    private val testUser = User(testId)
    private val validUserStorage: KeynotedexStorage = mock {
        on { retrieveUser(eq(testId), anyOrNull()) } doReturn testUser
    }

    private fun TestApplicationEngine.endpoint(userId: String) = application.locations.href(UserEndpoint(userId))

    @Test
    @Ignore("It's actually unhandled, the framework is the one doing the 404")
    fun `when requesting with no headers and no userId then it returns not found`() = testApp {
        handleRequest(HttpMethod.Put, endpoint("")).apply {
            assertThat(response.status(), equalTo(HttpStatusCode.NotFound))
        }
    }

    @Test
    @Ignore("It's actually unhandled, the framework is the one doing the 404")
    fun `when requesting with headers and no userId then it returns not found`() = testApp {
        handleRequest(HttpMethod.Put, endpoint("")) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.NotFound))
        }
    }

    @Test
    fun `when requesting with auth and no body then it returns server error`() = testApp {
        handleRequest(HttpMethod.Put, endpoint(testId)) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addAuthHeader(JwtConfig(application.environment), testId)
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.InternalServerError))
        }
    }

    @Test
    fun `when requesting with headers and invalid credentials then it returns unauthorized`() = testApp {
        handleRequest(HttpMethod.Put, endpoint(testId)) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(JSON.stringify(UserProfileUpdateRequest.serializer(), UserProfileUpdateRequest(testUser.toDto())))
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.Unauthorized))
        }
    }

    @Test
    fun `when requesting with headers and valid userId then it is accepted`() = testApp(validUserStorage) {
        handleRequest(HttpMethod.Put, endpoint(testId)) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addAuthHeader(JwtConfig(application.environment), testId)
            setBody(Json.stringify(UserProfileUpdateRequest.serializer(), UserProfileUpdateRequest(testUser.toDto())))
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.Accepted))
        }
    }

    @Test
    fun `when requesting with headers and valid userId then it returns such user`() = testApp(validUserStorage) {
        handleRequest(HttpMethod.Put, endpoint(testId)) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addAuthHeader(JwtConfig(application.environment), testId)
            setBody(Json.stringify(UserProfileUpdateRequest.serializer(), UserProfileUpdateRequest(testUser.toDto())))
        }.apply {
            assertThat(Json.parse(UserProfileResponse.serializer(), response.content!!).user, equalTo(testUser.toDto()))
        }
    }
}
