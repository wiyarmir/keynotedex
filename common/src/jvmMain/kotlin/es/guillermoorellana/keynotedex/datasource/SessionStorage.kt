package es.guillermoorellana.keynotedex.datasource

actual class SessionStorage {
    var proxy: SessionStorageProxy =
        object : SessionStorageProxy {
            override fun put(value: String?): Unit = TODO()
            override fun get(): String? = TODO()
            override fun clear(): Unit = TODO()
        }

    actual fun put(value: String?): Unit = proxy.put(value)
    actual fun get(): String? = proxy.get()
    actual fun clear(): Unit = proxy.clear()
}

interface SessionStorageProxy {
    fun put(value: String?)
    fun get(): String?
    fun clear()
}
