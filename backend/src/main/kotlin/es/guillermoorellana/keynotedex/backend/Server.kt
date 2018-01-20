package es.guillermoorellana.keynotedex.backend

import io.ktor.server.engine.*
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}
