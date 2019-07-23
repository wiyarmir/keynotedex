package es.guillermoorellana.keynotedex.datasource

import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

actual class SessionStorage {
    private val backingStorage = NSUserDefaults()

    actual fun put(value: String?) = backingStorage.setValue(value, forKey = KEY_SESSION)

    actual fun get(): String? = backingStorage.stringForKey(KEY_SESSION)

    actual fun clear() = backingStorage.removeObjectForKey(KEY_SESSION)
}
