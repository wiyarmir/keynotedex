package es.guillermoorellana.keynotedex.web.comms

import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.browser.sessionStorage

object SessionStorage {

    fun put(value: String?) =
        value?.let { sessionStorage[KEY_SESSION] = it } ?: sessionStorage.removeItem(KEY_SESSION)

    fun get(): String? = sessionStorage[KEY_SESSION]
}

private const val KEY_SESSION = "session"
