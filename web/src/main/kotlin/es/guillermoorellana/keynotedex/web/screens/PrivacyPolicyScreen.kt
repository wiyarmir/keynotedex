package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.web.components.divWithHtml
import kotlinext.js.require
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div

private val html = require("privacypolicy.html")

class PrivacyPolicyScreen : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div("row justify-content-center") {
            div("col-10 col-offset-1 col-sm-9 col-xl-8") {
                divWithHtml(classes = "", html = html)
            }
        }
    }
}
