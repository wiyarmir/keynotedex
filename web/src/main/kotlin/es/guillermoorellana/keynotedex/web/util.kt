package es.guillermoorellana.keynotedex.web

import kotlinext.js.*
import react.*
import react.dom.div

inline fun <T : RState> React.Component<*, T>.setState(action: T.() -> Unit) {
    setState(jsObject(action))
}

inline fun <T : RState> React.Component<*, T>.updateState(action: T.() -> Unit) {
    setState(clone(state).apply(action))
}

inline fun <T> RBuilder.loading(value: T?, action: (T) -> Unit) {
    if (value == null) {
        div(classes = "loading") {
            +"Loading data..."
        }
    } else {
        action(value)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Double.toFixed(precision: Int): String = asDynamic().toFixed(precision)
