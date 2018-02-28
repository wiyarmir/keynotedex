package es.guillermoorellana.keynotedex.web.screens

import kotlinx.html.*
import react.*
import react.dom.*

class HomeScreen : RComponent<HomeScreen.Props, HomeScreen.State>() {

    private val jumboHeading = "Jumbo heading"
    private val lead =
        "Cras justo odio, dapibus ac facilisis in, egestas eget quam. " +
                "Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, " +
                "ut fermentum massa justo sit amet risus."

    init {
        state = State()
    }

    override fun RBuilder.render() {
        main {
            attrs { role = "main" }
            div("jumbotron") {
                div("container") {

                    h1("display-3") { +jumboHeading }
                    p("lead") { +lead }
                    p { a(href = "#", classes = "btn btn-primary btn-lg") { +"Sign up" } }
                }
            }
            div("container") {
                div("row") {
                    div("col-md-4") {
                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }
                        p { a(href = "#", classes = "btn btn-secondary btn-lg") { +"View details" } }
                    }
                    div("col-md-4") {
                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }
                        p { a(href = "#", classes = "btn btn-secondary btn-lg") { +"View details" } }
                    }
                    div("col-md-4") {
                        h4 { +"Subheading" }
                        p { +"Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum." }
                        p { a(href = "#", classes = "btn btn-secondary btn-lg") { +"View details" } }
                    }
                }
            }
        }
    }

    class Props : RProps

    class State : RState
}
