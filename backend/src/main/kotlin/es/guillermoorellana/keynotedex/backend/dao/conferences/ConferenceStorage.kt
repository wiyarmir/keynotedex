package es.guillermoorellana.keynotedex.backend.dao.conferences

import java.io.*

interface ConferenceStorage : Closeable {
    fun conference(conferenceId: String): Conference?
    fun conferences(): List<Conference>
}
