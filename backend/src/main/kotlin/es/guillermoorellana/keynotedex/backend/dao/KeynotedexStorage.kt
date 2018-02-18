package es.guillermoorellana.keynotedex.backend.dao

import es.guillermoorellana.keynotedex.backend.dao.conferences.*
import es.guillermoorellana.keynotedex.backend.dao.submissions.*
import es.guillermoorellana.keynotedex.backend.dao.users.*
import java.io.*

interface KeynotedexStorage :
    Closeable,
    UserStorage,
    ConferenceStorage,
    SubmissionStorage


