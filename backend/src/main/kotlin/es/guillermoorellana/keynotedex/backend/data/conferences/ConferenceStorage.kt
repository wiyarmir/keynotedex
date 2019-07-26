package es.guillermoorellana.keynotedex.backend.data.conferences

import java.io.Closeable

interface ConferenceStorage : Closeable {
    fun conference(conferenceId: Long): Conference?
    fun conferences(): List<Conference>

    fun putAll(conferences: List<Conference>)
}
