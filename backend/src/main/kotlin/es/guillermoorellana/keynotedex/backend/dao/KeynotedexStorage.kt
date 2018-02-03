package es.guillermoorellana.keynotedex.backend.dao

import es.guillermoorellana.keynotedex.backend.conference.*
import es.guillermoorellana.keynotedex.backend.submission.*
import es.guillermoorellana.keynotedex.backend.user.*
import java.io.*

interface KeynotedexStorage :
    Closeable,
    UserStorage,
    ConferenceStorage,
    SubmissionStorage


