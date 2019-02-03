package es.guillermoorellana.keynotedex.backend.api.signin

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import es.guillermoorellana.keynotedex.backend.auth.JwtTokenProvider
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.testApp
import es.guillermoorellana.keynotedex.responses.LoginResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.formUrlEncode
import io.ktor.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlinx.serialization.json.JSON
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PostSignInTest {

    private val testId = "testId"
    private val testPassword = "longTestPassword"
    private val testToken = "testToken"
    private val testUser = User(testId)
    private val mockStorage: KeynotedexStorage = mock { }
    private val mockJwtProvider: JwtTokenProvider = mock {}
    private val TestApplicationEngine.endpoint
        get() = application.locations.href(SignInEndpoint())

    @Test
    fun `when logging in with non existent user then returns unauthorized`() = testApp(mockStorage) {
        handleRequest(HttpMethod.Post, endpoint) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to testId, "password" to testPassword).formUrlEncode())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.Unauthorized))
        }
    }

    @Test
    fun `when logging in with valid user then returns ok`() = testApp(mockStorage) {
        whenever(mockStorage.retrieveUser(eq(testId), any())) doReturn testUser
        handleRequest(HttpMethod.Post, endpoint) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to testId, "password" to testPassword).formUrlEncode())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.OK))
        }
    }

    @Test
    fun `when logging in with valid user then jwt token is set`() = testApp(mockStorage, mockJwtProvider) {
        whenever(mockStorage.retrieveUser(eq(testId), any())) doReturn testUser
        whenever(mockJwtProvider.invoke(eq(testId))) doReturn testToken
        handleRequest(HttpMethod.Post, endpoint) {
            addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to testId, "password" to testPassword).formUrlEncode())
        }.apply {
            assertThat(
                JSON.parse(LoginResponse.serializer(), response.content!!),
                equalTo(LoginResponse(testToken))
            )
        }
    }
}
