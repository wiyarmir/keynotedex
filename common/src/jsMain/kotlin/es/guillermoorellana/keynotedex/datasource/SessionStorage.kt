package es.guillermoorellana.keynotedex.datasource

import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.browser.sessionStorage

actual class SessionStorage {

    private val backingStorage = sessionStorage

    actual fun put(value: String?) =
        value?.let { backingStorage[KEY_SESSION] = it } ?: backingStorage.removeItem(KEY_SESSION)

    actual fun get(): String? = backingStorage[KEY_SESSION]

    actual fun clear() = backingStorage.removeItem(KEY_SESSION)
}
