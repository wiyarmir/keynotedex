package es.guillermoorellana.keynotedex.web

import react.*
import react.dom.*

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
