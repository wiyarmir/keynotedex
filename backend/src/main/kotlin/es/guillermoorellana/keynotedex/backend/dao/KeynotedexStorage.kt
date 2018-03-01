package es.guillermoorellana.keynotedex.backend.dao

import es.guillermoorellana.keynotedex.backend.dao.conferences.ConferenceStorage
import es.guillermoorellana.keynotedex.backend.dao.submissions.SubmissionStorage
import es.guillermoorellana.keynotedex.backend.dao.users.UserStorage
import java.io.Closeable

interface KeynotedexStorage :
    Closeable,
    UserStorage,
    ConferenceStorage,
    SubmissionStorage


