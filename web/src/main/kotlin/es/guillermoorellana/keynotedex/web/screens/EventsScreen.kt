package es.guillermoorellana.keynotedex.web.screens

import arrow.core.Try
import es.guillermoorellana.keynotedex.repository.model.Conference
import es.guillermoorellana.keynotedex.web.components.divWithHtml
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
                {
                    p { +"Failed to get events: ${it.message}" }
                    Unit
                },
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

private fun RBuilder.renderTwitterLogo() {
    //language=HTML
    divWithHtml(
        html = """
<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" class="twtr-icon  twtr-color-fill--blue-dark  has-hover"> 
 <path opacity="0" d="M0 0h24v24H0z"></path> 
 <path d="M23.643 4.937c-.835.37-1.732.62-2.675.733.962-.576 1.7-1.49 2.048-2.578-.9.534-1.897.922-2.958 1.13-.85-.904-2.06-1.47-3.4-1.47-2.572 0-4.658 2.086-4.658 4.66 0 .364.042.718.12 1.06-3.873-.195-7.304-2.05-9.602-4.868-.4.69-.63 1.49-.63 2.342 0 1.616.823 3.043 2.072 3.878-.764-.025-1.482-.234-2.11-.583v.06c0 2.257 1.605 4.14 3.737 4.568-.392.106-.803.162-1.227.162-.3 0-.593-.028-.877-.082.593 1.85 2.313 3.198 4.352 3.234-1.595 1.25-3.604 1.995-5.786 1.995-.376 0-.747-.022-1.112-.065 2.062 1.323 4.51 2.093 7.14 2.093 8.57 0 13.255-7.098 13.255-13.254 0-.2-.005-.402-.014-.602.91-.658 1.7-1.477 2.323-2.41z"></path> 
</svg>
""".trimIndent()
    )
}

external interface EventsScreenProps : WithNetworkRepository
external interface EventsScreenState : RState {
    var events: Try<List<Conference>>?
}

fun RBuilder.eventsScreen(builder: RHandler<EventsScreenProps>) = child(EventsScreen::class, builder)
