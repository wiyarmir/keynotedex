package es.guillermoorellana.keynotedex.web

import react.RBuilder
import react.dom.div

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
