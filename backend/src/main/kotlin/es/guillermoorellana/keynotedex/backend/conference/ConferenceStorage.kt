package es.guillermoorellana.keynotedex.backend.conference

import es.guillermoorellana.keynotedex.backend.dao.tables.*
import java.io.*

interface ConferenceStorage : Closeable {
    fun conference(conferenceId: String): Conference?
    fun conferences(): List<Conference>
}
