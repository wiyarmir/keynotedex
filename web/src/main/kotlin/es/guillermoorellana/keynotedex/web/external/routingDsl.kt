package es.guillermoorellana.keynotedex.web.external

import react.*
import kotlin.reflect.KClass

fun RBuilder.hashRouter(handler: RHandler<RProps>) = child(HashRouterComponent::class, handler)

fun RBuilder.switch(handler: RHandler<RProps>) = child(SwitchComponent::class, handler)

fun RBuilder.route(path: String, component: KClass<out React.Component<*, *>>, exact: Boolean = false) =
    child(RouteComponent::class) {
        attrs {
            this.path = path
            this.exact = exact
            this.component = component.js.unsafeCast<RClass<RProps>>()
        }
    }

fun RBuilder.route(path: String, exact: Boolean = false, render: (props: RouteResultProps<*>) -> ReactElement) =
    child(RouteComponent::class) {
        attrs {
            this.path = path
            this.exact = exact
            this.render = render
        }
    }

fun RBuilder.routeLink(to: String, handler: RHandler<RProps>) = child(LinkComponent::class) {
    attrs {
        this.to = to
    }
    handler()
}

fun RBuilder.redirect(to: String, handler: RHandler<RProps>) = child(RedirectComponent::class) {
    attrs {
        this.to = to
    }
    handler()
}
