package es.guillermoorellana.keynotedex.web.components.submissions

import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.model.Session
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.h4
import react.dom.p

class SessionPreview : RComponent<SessionPreviewProps, RState>() {
    override fun RBuilder.render() {
        with(props.submission) {
            div("col-md-4") {
                h4 { +title }
                p { +abstract }
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
    var submission: Session
}

fun RBuilder.sessionPreview(handler: RHandler<SessionPreviewProps>) = child(SessionPreview::class, handler)
