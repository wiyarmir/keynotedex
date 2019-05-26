package es.guillermoorellana.keynotedex.web.context

import es.guillermoorellana.keynotedex.repository.model.User
import react.createContext

val UserContext = createContext<User?>(null)
