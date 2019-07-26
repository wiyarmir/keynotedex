package es.guillermoorellana.keynotedex.datasource

data class NetworkConfig(
    val host: String = "localhost",
    val port: Int = 8080,
    val secure: Boolean = false
)
