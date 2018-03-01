package es.guillermoorellana.keynotedex.backend

import io.ktor.application.Application
import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun Application.sessionKey(): ByteArray {
    val config = environment.config.config("keynotedex")
    val sessionCookieConfig = config.config("session.cookie")
    val key: String = sessionCookieConfig.property("key").getString()
    return hex(key)
}

fun Application.hashPassword(password: String) = hash(password, secretKeySpec())

private fun Application.secretKeySpec() = SecretKeySpec(sessionKey(), "HmacSHA1")

private fun hash(password: String, secretKeySpec: SecretKeySpec): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(secretKeySpec)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
