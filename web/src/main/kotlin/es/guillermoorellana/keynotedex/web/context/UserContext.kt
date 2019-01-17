package es.guillermoorellana.keynotedex.web.context

import es.guillermoorellana.keynotedex.web.model.User
import react.createContext

val UserContext = createContext<User?>(null)
