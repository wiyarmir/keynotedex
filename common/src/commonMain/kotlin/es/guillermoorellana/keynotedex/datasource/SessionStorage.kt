package es.guillermoorellana.keynotedex.datasource

expect class SessionStorage {
    fun put(value: String?)
    fun get(): String?
    fun clear()
}

internal const val KEY_SESSION = "sessionToken"
