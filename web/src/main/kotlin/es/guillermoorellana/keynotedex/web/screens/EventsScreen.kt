package es.guillermoorellana.keynotedex.web.screens

import arrow.core.Try
import es.guillermoorellana.keynotedex.repository.model.Conference
import es.guillermoorellana.keynotedex.web.renderTwitterLogo
import es.guillermoorellana.keynotedex.web.repository.WithNetworkRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.ThScope.col
import kotlinx.html.ThScope.colGroup
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RState
import react.dom.a
import react.dom.div
import react.dom.p
import react.dom.style
import react.dom.table
import react.dom.tbody
import react.dom.td
import react.dom.th
import react.dom.thead
import react.dom.tr
import react.setState

//language=CSS
val style = """
.twtr-icon {
    width: 24px;
    height: 24px;
}
.twtr-color-fill--blue-dark {
    fill: #1da1f2;
}
""".trimIndent()

class EventsScreen : RComponent<EventsScreenProps, EventsScreenState>() {

    init {
        GlobalScope.launch {
            val events = props.networkRepository.getEvents()
            setState {
                this.events = events
            }
        }
    }

    override fun RBuilder.render() {
        style { +style }
        div("container") {
            state.events?.fold(
                { p { +"Failed to get events: ${it.message}" } },
                { renderTable(it) }
            )
        }
    }

    private fun RBuilder.renderTable(conferences: List<Conference>) {
        conferences.ifEmpty { p { +"No events yet." } }
        table("table") {
            thead {
                tr {
                    th(scope = col) { +"Start" }
                    th(scope = col) { +"End" }
                    th(scope = colGroup) {
                        attrs { colSpan = "2" }
                        +"Name"
                    }
                    th(scope = col) { +"Location" }
                    th(scope = col) { +"Call for Papers" }
                }
            }
            tbody {
                conferences.forEach { renderRow(it) }
            }
        }
    }

    private fun RBuilder.renderRow(conference: Conference) = with(conference) {
        tr {
            td { +dateStart }
            td { +dateEnd }
            td { twitter?.let { a(href = it, target = "_blank") { renderTwitterLogo() } } }
            td { a(href = website, target = "_blank") { +name } }
            td { +(location ?: "") }
            td { cfpSite?.let { a(href = it, target = "_blank") { +"CfP Site" } } }
        }
    }
}

external interface EventsScreenProps : WithNetworkRepository
external interface EventsScreenState : RState {
    var events: Try<List<Conference>>?
}

fun RBuilder.eventsScreen(builder: RHandler<EventsScreenProps>) = child(EventsScreen::class, builder)
