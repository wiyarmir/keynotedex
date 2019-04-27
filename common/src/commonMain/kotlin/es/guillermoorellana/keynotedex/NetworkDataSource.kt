package es.guillermoorellana.keynotedex

import arrow.core.Try
import es.guillermoorellana.keynotedex.api.Api.V1.Paths
import es.guillermoorellana.keynotedex.dto.Submission
import es.guillermoorellana.keynotedex.requests.SubmissionCreateRequest
import es.guillermoorellana.keynotedex.requests.SubmissionUpdateRequest
import es.guillermoorellana.keynotedex.requests.UserProfileUpdateRequest
import es.guillermoorellana.keynotedex.responses.SignInResponse
import es.guillermoorellana.keynotedex.responses.SubmissionResponse
import es.guillermoorellana.keynotedex.responses.UserProfileResponse
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

class NetworkDataSource(
    private val tokenProvider: () -> String?,
    private val apiHost: String = "localhost",
    private val apiPort: Int = 8080,
    private val httpClient: HttpClient = makeHttpClient(apiHost, apiPort, tokenProvider)
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

    suspend fun getSubmission(submissionId: String): Try<Submission> = Try {
        httpClient.get<Submission>(
            path = Paths.submissions.replace("{submissionId?}", submissionId)
        )
    }

    suspend fun postSubmission(submissionCreateRequest: SubmissionCreateRequest): Try<Unit> = Try {
        httpClient.post<Unit>(
            path = Paths.submissions.replace("{submissionId?}", ""),
            body = submissionCreateRequest
        )
    }

    suspend fun updateSubmission(submission: Submission): Try<Unit> = Try {
        httpClient.put<Unit>(
            path = Paths.submissions.replace("{submissionId?}", ""),
            body = submission
        )
    }
}

private fun makeHttpClient(apiHost: String, apiPort: Int, tokenProvider: () -> String?): HttpClient = HttpClient {
    defaultRequest {
        host = apiHost
        port = apiPort
        if (tokenProvider() != null) header(HttpHeaders.Authorization, "Bearer ${tokenProvider()}")
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer().apply {
            register<SignInResponse>()
            register<UserProfileResponse>()
            register<SubmissionResponse>()
            register<SubmissionCreateRequest>()
            register<SubmissionUpdateRequest>()
            register(EmptyContentSerializer())
        }
    }
}

private class EmptyContentSerializer : KSerializer<EmptyContent> {
    override val descriptor: SerialDescriptor = UnitDescriptor

    override fun deserialize(decoder: Decoder): EmptyContent {
        decoder.decodeUnit()
        return EmptyContent
    }

    override fun serialize(encoder: Encoder, obj: EmptyContent) {
        encoder.encodeUnit()
    }
}
