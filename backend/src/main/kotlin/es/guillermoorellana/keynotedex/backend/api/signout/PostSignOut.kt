package es.guillermoorellana.keynotedex.backend.api.signout

import es.guillermoorellana.keynotedex.backend.JsonSerializableConverter
import es.guillermoorellana.keynotedex.datasource.responses.SignOutResponse
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept

@UseExperimental(KtorExperimentalLocationsAPI::class)
fun Route.postSignOut() {

    JsonSerializableConverter.register(SignOutResponse.serializer())

    accept(ContentType.Application.Json) {
        post<SignOutEndpoint> {
            call.respond(SignOutResponse())
        }
    }
}
