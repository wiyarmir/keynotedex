package es.guillermoorellana.keynotedex.datasource

import arrow.core.Try
import es.guillermoorellana.keynotedex.api.Api.V1.Paths
import es.guillermoorellana.keynotedex.datasource.dto.Session
import es.guillermoorellana.keynotedex.datasource.requests.SessionCreateRequest
import es.guillermoorellana.keynotedex.datasource.requests.SessionUpdateRequest
import es.guillermoorellana.keynotedex.datasource.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.datasource.responses.SessionResponse
import es.guillermoorellana.keynotedex.datasource.responses.SignInResponse
import es.guillermoorellana.keynotedex.datasource.responses.UserProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.host
import io.ktor.client.request.port
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.utils.EmptyContent
import io.ktor.http.HttpHeaders
import io.ktor.http.parametersOf
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.UnitDescriptor
import kotlinx.serialization.json.Json

class NetworkDataSource(
    sessionStorage: SessionStorage,
    private val httpClient: HttpClient = makeHttpClient("localhost", 8080, sessionStorage)
) {

    suspend fun register(userId: String, password: String, displayName: String, email: String): Try<SignInResponse> =
        Try {
            httpClient.submitForm<SignInResponse>(
                path = Paths.signUp,
                formData = parametersOf(
                    "userId" to listOf(userId),
                    "password" to listOf(password),
                    "displayName" to listOf(displayName),
                    "email" to listOf(email)
                )
            )
        }

    suspend fun userProfile(userId: String): Try<UserProfileResponse> = Try {
        httpClient.get<UserProfileResponse>(
            path = Paths.user.replace("{userId}", userId)
        )
    }

    suspend fun updateUserProfile(userProfile: UserProfileUpdateRequest): Try<UserProfileResponse> = Try {
        httpClient.put<UserProfileResponse>(
            path = Paths.user.replace("{userId}", userProfile.user.userId),
            body = userProfile
        )
    }

    suspend fun login(userId: String, password: String): Try<SignInResponse> = Try {
        httpClient.submitForm<SignInResponse>(
            path = Paths.signIn,
            formData = parametersOf(
                "userId" to listOf(userId),
                "password" to listOf(password)
            )
        )
    }

    suspend fun logoutUser(): Try<Unit> = Try {
        httpClient.get<Unit>(
            path = Paths.signOut
        )
    }

    suspend fun getSubmission(submissionId: String): Try<SessionResponse> = Try {
        httpClient.get<SessionResponse>(
            path = Paths.sessions.replace("{sessionId?}", submissionId)
        )
    }

    suspend fun postSubmission(sessionCreateRequest: SessionCreateRequest): Try<Unit> = Try {
        httpClient.post<Unit>(
            path = Paths.sessions.replace("{sessionId?}", ""),
            body = sessionCreateRequest
        )
    }

    suspend fun updateSubmission(session: Session): Try<Unit> = Try {
        httpClient.put<Unit>(
            path = Paths.sessions.replace("{sessionId?}", ""),
            body = session
        )
    }
}

private fun makeHttpClient(
    apiHost: String,
    apiPort: Int,
    sessionStorage: SessionStorage
): HttpClient = HttpClient {
    defaultRequest {
        host = apiHost
        port = apiPort
        sessionStorage.get()?.let { token ->
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer(json = Json.nonstrict).apply {
            register<SignInResponse>()
            register<UserProfileResponse>()
            register<SessionResponse>()
            register<SessionCreateRequest>()
            register<SessionUpdateRequest>()
            register(EmptyContentSerializer)
        }
    }
}

private object EmptyContentSerializer : KSerializer<EmptyContent> {
    override val descriptor: SerialDescriptor = UnitDescriptor

    override fun deserialize(decoder: Decoder): EmptyContent = EmptyContent.also { decoder.decodeUnit() }

    override fun serialize(encoder: Encoder, obj: EmptyContent) = encoder.encodeUnit()
}
