package es.guillermoorellana.keynotedex.web.repository

import es.guillermoorellana.keynotedex.repository.NetworkRepository
import react.RProps

external interface WithNetworkRepository : RProps {
    var networkRepository: NetworkRepository
}
