package es.guillermoorellana.keynotedex.web.components.sessions

import es.guillermoorellana.keynotedex.repository.model.SessionPreview
import es.guillermoorellana.keynotedex.web.external.routeLink
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.h4
import react.dom.p

class SessionPreviewComponent : RComponent<SessionPreviewProps, RState>() {
    override fun RBuilder.render() {
        with(props.preview) {
            div("col-md-4") {
                h4 { +title }
                p { +"By $userDisplayName" }
                p {
                    routeLink(to = "$userId/$sessionId") {
                        attrs {
                            className = "btn btn-secondary btn-lg"
                        }
                        +"View details"
                    }
                }
            }
        }
    }
}

external interface SessionPreviewProps : RProps {
    var preview: SessionPreview
}

fun RBuilder.sessionPreview(handler: RHandler<SessionPreviewProps>) = child(SessionPreviewComponent::class, handler)
