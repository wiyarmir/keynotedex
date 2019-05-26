package es.guillermoorellana.keynotedex.datasource

@Suppress("NotImplementedDeclaration", "OptionalUnit")
actual open class SessionStorage {
    actual open fun put(value: String?): Unit = TODO()

    actual open fun get(): String? = TODO()

    actual open fun clear(): Unit = TODO()
}
