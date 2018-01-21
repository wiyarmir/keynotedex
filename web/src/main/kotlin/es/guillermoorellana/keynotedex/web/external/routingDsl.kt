package es.guillermoorellana.keynotedex.web.external

import react.*
import kotlin.reflect.KClass

@ReactDsl
fun RBuilder.hashRouter(handler: RHandler<RProps>) = child(HashRouterComponent::class, handler)

@ReactDsl
fun RBuilder.switch(handler: RHandler<RProps>) = child(SwitchComponent::class, handler)

@ReactDsl
fun RBuilder.route(path: String, component: KClass<out React.Component<*, *>>, exact: Boolean = false) =
    child(RouteComponent::class) {
        attrs {
            this.path = path
            this.exact = exact
            this.component = component.js.unsafeCast<RClass<RProps>>()
        }
    }

@ReactDsl
fun RBuilder.route(path: String, exact: Boolean = false, render: (props: RouteResultProps<*>) -> ReactElement) =
    child(RouteComponent::class) {
        attrs {
            this.path = path
            this.exact = exact
            this.render = render
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
