import kotlinx.html.div
import kotlinx.html.h1
import react.ReactComponentNoProps
import react.ReactComponentNoState
import react.ReactComponentSpec
import react.dom.ReactDOM
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    runtime.wrappers.require("pure-blog.css")

    window.onload = {
        ReactDOM.render(document.getElementById("content")) {
            div {
                Application {}
            }
        }
    }
}

class Application : ReactDOMComponent<ReactComponentNoProps, ReactComponentNoState>() {
    companion object : ReactComponentSpec<Application, ReactComponentNoProps, ReactComponentNoState>

    init {
        state = ReactComponentNoState()
    }

    override fun ReactDOMBuilder.render() {
        h1 { +"Loading" }
    }
}
