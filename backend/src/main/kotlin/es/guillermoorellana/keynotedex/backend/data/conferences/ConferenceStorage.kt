package es.guillermoorellana.keynotedex.backend.data.conferences

import java.io.Closeable

interface ConferenceStorage : Closeable {
    fun conference(conferenceId: String): Conference?
    fun conferences(): List<Conference>
}
