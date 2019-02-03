package es.guillermoorellana.keynotedex.web.external

import react.Component
import react.RBuilder
import react.RClass
import react.RHandler
import react.RProps
import react.ReactDsl
import react.ReactElement
import kotlin.reflect.KClass

@ReactDsl
fun RBuilder.hashRouter(handler: RHandler<RProps>) = child(HashRouterComponent::class, handler)

@ReactDsl
fun RBuilder.browserRouter(handler: RHandler<RProps>) = child(BrowserRouterComponent::class, handler)

@ReactDsl
fun RBuilder.switch(handler: RHandler<RProps>) = child(SwitchComponent::class, handler)

@ReactDsl
fun RBuilder.route(component: KClass<out Component<*, *>>): ReactElement =
    child<RouteProps<RProps>, RouteComponent<RProps>> {
        attrs {
            this.component = component.js.unsafeCast<RClass<RProps>>()
        }
    }

@ReactDsl
fun RBuilder.route(
    path: String,
    component: KClass<out Component<*, *>>,
    exact: Boolean = false
): ReactElement =
    child<RouteProps<RProps>, RouteComponent<RProps>> {
        attrs {
            this.path = path
            this.exact = exact
            this.component = component.js.unsafeCast<RClass<RProps>>()
        }
    }

@ReactDsl
fun <T : RProps> RBuilder.route(
    path: String,
    exact: Boolean = false,
    render: (props: RouteResultProps<T>) -> ReactElement?
): ReactElement =
    child<RouteProps<T>, RouteComponent<T>> {
        attrs {
            this.path = path
            this.exact = exact
            this.render = render
        }
    }

@ReactDsl
fun RBuilder.route(path: String, exact: Boolean = false, render: () -> ReactElement?): ReactElement =
    child<RouteProps<RProps>, RouteComponent<RProps>> {
        attrs {
            this.path = path
            this.exact = exact
            this.render = { render() }
        }
    }

@ReactDsl
fun RBuilder.routeLink(to: String, handler: RHandler<LinkProps>) = child(LinkComponent::class) {
    attrs {
        this.to = to
    }
    handler()
}

@ReactDsl
fun RBuilder.redirect(to: String, handler: RHandler<RProps>) = child(RedirectComponent::class) {
    attrs {
        this.to = to
    }
    handler()
}
