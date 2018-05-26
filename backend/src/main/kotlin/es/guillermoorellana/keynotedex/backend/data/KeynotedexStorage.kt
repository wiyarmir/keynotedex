package es.guillermoorellana.keynotedex.backend.data

import es.guillermoorellana.keynotedex.backend.data.conferences.*
import es.guillermoorellana.keynotedex.backend.data.submissions.*
import es.guillermoorellana.keynotedex.backend.data.users.*
import java.io.*

interface KeynotedexStorage :
    Closeable,
    UserStorage,
    ConferenceStorage,
    SubmissionStorage
