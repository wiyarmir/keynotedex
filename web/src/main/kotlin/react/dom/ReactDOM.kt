package react.dom

import org.w3c.dom.Element
import react.RProps
import react.RState
import react.ReactComponent
import react.ReactElement

@JsModule("react-dom")
external object ReactDOM {
    fun render(element: ReactElement?, container: Element?)
    fun <P : RProps, S : RState> findDOMNode(component: ReactComponent<P, S>): Element
    fun unmountComponentAtNode(domContainerNode: Element?)
}

fun ReactDOM.render(container: Element?, handler: ReactDOMBuilder.() -> Unit) =
        render(buildElement(handler), container)
