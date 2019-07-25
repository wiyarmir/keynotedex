package es.guillermoorellana.keynotedex.web.screens

import arrow.core.Try
import es.guillermoorellana.keynotedex.repository.model.Conference
import es.guillermoorellana.keynotedex.web.repository.WithNetworkRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RState
import react.dom.div
import react.dom.p
import react.setState

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
        div("container") {
            state.events?.fold(
                {
                    p { +"Failed to get events: ${it.message}" }
                    Unit
                },
                {
                    it.ifEmpty { p { +"No events yet." } }
                    it.forEach { p { +"${it.name}" } }
                }
            )

        }
    }
}

external interface EventsScreenProps : WithNetworkRepository
external interface EventsScreenState : RState {
    var events: Try<List<Conference>>?
}

fun RBuilder.eventsScreen(builder: RHandler<EventsScreenProps>) = child(EventsScreen::class, builder)
