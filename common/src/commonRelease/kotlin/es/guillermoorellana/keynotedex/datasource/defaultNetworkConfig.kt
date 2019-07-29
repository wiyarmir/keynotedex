package es.guillermoorellana.keynotedex.datasource

data class NetworkConfig(
    val host: String = "keynotedex.wiyarmir.es",
    val port: Int = 0,
    val secure: Boolean = true
)
