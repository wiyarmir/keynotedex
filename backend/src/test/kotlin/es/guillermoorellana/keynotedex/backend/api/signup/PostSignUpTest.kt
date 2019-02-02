package es.guillermoorellana.keynotedex.backend.api.signup

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import es.guillermoorellana.keynotedex.backend.data.KeynotedexStorage
import es.guillermoorellana.keynotedex.backend.data.users.User
import es.guillermoorellana.keynotedex.backend.testApp
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.formUrlEncode
import io.ktor.locations.locations
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PostSignUpTest {

    private val testId = "testId"
    private val testPassword = "longTestPassword"
    private val testUser = User(testId)
    private val mockStorage: KeynotedexStorage = mock { }

    private val TestApplicationEngine.endpoint
        get() = application.locations.href(SignUpEndpoint())

    @Test
    fun `when userId and password provided then user is created`() = testApp(mockStorage) {
        handleRequest(HttpMethod.Post, endpoint) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to testId, "password" to testPassword).formUrlEncode())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.Created))
        }
    }

    @Test
    fun `when password is short then precondition failed`() = testApp(mockStorage) {
        handleRequest(HttpMethod.Post, endpoint) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to testId, "password" to testPassword.substring(0..2)).formUrlEncode())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.PreconditionFailed))
        }
    }

    @Test
    fun `when userId is short then precondition failed`() = testApp(mockStorage) {
        handleRequest(HttpMethod.Post, endpoint) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to testId.substring(0..2), "password" to testPassword).formUrlEncode())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.PreconditionFailed))
        }
    }

    @Test
    fun `when userId is invalid then precondition failed`() = testApp(mockStorage) {
        handleRequest(HttpMethod.Post, endpoint) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to "%%%^&*()£", "password" to testPassword).formUrlEncode())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.PreconditionFailed))
        }
    }

    @Test
    fun `when data is provided and user exists then returns conflict`() = testApp(mockStorage) {
        whenever(mockStorage.retrieveUser(eq(testId), anyOrNull())) doReturn testUser
        handleRequest(HttpMethod.Post, endpoint) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to testId, "password" to testPassword).formUrlEncode())
        }.apply {
            assertThat(response.status(), equalTo(HttpStatusCode.Conflict))
        }
    }
}
