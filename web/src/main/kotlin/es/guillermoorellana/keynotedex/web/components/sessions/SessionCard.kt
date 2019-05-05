package es.guillermoorellana.keynotedex.web.components.sessions

import es.guillermoorellana.keynotedex.repository.model.Session
import es.guillermoorellana.keynotedex.repository.model.SessionVisibility.PRIVATE
import es.guillermoorellana.keynotedex.repository.model.SessionVisibility.PUBLIC
import es.guillermoorellana.keynotedex.web.external.routeLink
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.h3
import react.dom.p

class SessionCard : RComponent<SessionCardProps, RState>() {
    override fun RBuilder.render() {
        with(props.session) {
            div("col-12 col-sm-6 col-xl-4") {
                routeLink(to = "/$userId/$sessionSlug-$sessionId") { h3 { +title() } }
                abstract.let { if (it.isNotEmpty()) p { +it } }
                type.let { if (it.isNotEmpty()) p { +"Type $it" } }
                submittedTo.let { if (it.isNotEmpty()) p { +"Submitted to $it" } }
            }
        }
    }
}

private fun Session.title() = when (visibility) {
    PUBLIC -> title
    PRIVATE -> "\uD83D\uDD12 $title"
}

private val Session.sessionSlug: String
    get() = title
        .replace(' ', '-')
        .toLowerCase()
        .replace(Regex("((?!([a-z0-9\\-])).)"), "")

external interface SessionCardProps : RProps {
    var session: Session
}

fun RBuilder.sessionCard(handler: RHandler<SessionCardProps>) = child(SessionCard::class, handler)
