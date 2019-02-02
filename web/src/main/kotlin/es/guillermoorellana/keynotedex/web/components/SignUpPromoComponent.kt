package es.guillermoorellana.keynotedex.web.components

import es.guillermoorellana.keynotedex.web.external.routeLink
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.h1
import react.dom.p

class SignUpPromoComponent : RComponent<RProps, RState>() {
    private val jumboHeading = "All about talks"
    private val lead = "Share here the talk you want to do, the talk you will do and the talk you have done."

    override fun RBuilder.render() {
        div("jumbotron") {
            div("container") {
                h1("display-3") { +jumboHeading }
                p("lead") { +lead }
                p {
                    routeLink(to = "/signup") {
                        attrs {
                            className = "btn btn-primary btn-lg"
                        }
                        +"Sign Up"
                    }
                }
            }
        }
    }
}

fun RBuilder.signUpPromo(block: RHandler<RProps>) = child(SignUpPromoComponent::class, block)
