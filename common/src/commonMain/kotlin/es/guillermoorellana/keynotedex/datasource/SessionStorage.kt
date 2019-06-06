package es.guillermoorellana.keynotedex.datasource

@Suppress("UnusedPrivateMember", "EmptyDefaultConstructor")
expect class SessionStorage constructor() {
    fun put(value: String?)
    fun get(): String?
    fun clear()
}

const val KEY_SESSION = "sessionToken"
