package es.guillermoorellana.keynotedex.datasource

actual class SessionStorage(
    private val keyValueStorage: KeyValueStorage
) {
    actual fun put(value: String?) = keyValueStorage.save(KEY_SESSION, value)

    actual fun get(): String? = keyValueStorage.load(KEY_SESSION)

    actual fun clear() = keyValueStorage.save(KEY_SESSION, null)
}

interface KeyValueStorage {
    fun save(key: String, value: String?)
    fun load(key: String): String?
}
