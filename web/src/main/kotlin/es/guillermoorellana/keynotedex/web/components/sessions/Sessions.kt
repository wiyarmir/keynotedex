package es.guillermoorellana.keynotedex.web.components.sessions

import es.guillermoorellana.keynotedex.repository.model.Session
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.h2
import react.dom.hr
import react.dom.p

class Sessions : RComponent<SessionsProps, RState>() {
    override fun RBuilder.render() {
        h2 { +"Sessions" }
        hr { }
        div("row") {
            if (props.sessions.isEmpty()) {
                p { +"No sessions yet" }
            } else {
                props.sessions.forEach { sub ->
                    sessionCard {
                        attrs { session = sub }
                    }
                }
            }
        }
    }
}

external interface SessionsProps : RProps {
    var sessions: List<Session>
}

fun RBuilder.sessions(handler: RHandler<SessionsProps>) = child(Sessions::class, handler)
