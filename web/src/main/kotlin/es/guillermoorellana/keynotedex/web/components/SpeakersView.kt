package es.guillermoorellana.keynotedex.web.components

import react.*
import react.dom.h1

class SpeakersView : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        h1 { +"Speakers" }
    }
}
