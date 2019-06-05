package es.guillermoorellana.keynotedex.datasource

@Suppress("UnusedPrivateMember")
expect class SessionStorage {
    fun put(value: String?)
    fun get(): String?
    fun clear()
}

const val KEY_SESSION = "sessionToken"
