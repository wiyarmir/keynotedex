package es.guillermoorellana.keynotedex.datasource

actual class SessionStorage {
    var proxy: SessionStorageProxy =
        @Suppress("NotImplementedDeclaration")
        object : SessionStorageProxy {
            override fun put(value: String?) = TODO()
            override fun get(): String? = TODO()
            override fun clear() = TODO()
        }

    actual fun put(value: String?) = proxy.put(value)
    actual fun get(): String? = proxy.get()
    actual fun clear() = proxy.clear()
}

interface SessionStorageProxy {
    fun put(value: String?)
    fun get(): String?
    fun clear()
}
