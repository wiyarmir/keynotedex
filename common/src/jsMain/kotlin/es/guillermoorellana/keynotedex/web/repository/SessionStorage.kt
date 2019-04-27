package es.guillermoorellana.keynotedex.web.repository

import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.browser.sessionStorage

object SessionStorage {

    private val backingStorage = sessionStorage

    fun put(value: String?) =
        value?.let { backingStorage[KEY_SESSION] = it } ?: backingStorage.removeItem(KEY_SESSION)

    fun get(): String? = backingStorage[KEY_SESSION]

    fun clear() = backingStorage.removeItem(KEY_SESSION)
}

private const val KEY_SESSION = "sessionToken"

val sessionStorageTokenProvider = { sessionStorage: SessionStorage ->
    { sessionStorage.get() }
}
