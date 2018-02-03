package es.guillermoorellana.keynotedex.backend.conference

import java.io.*

interface ConferenceStorage : Closeable {
    fun conference(conferenceId: String): Conference?
    fun conferences(): List<Conference>
}
