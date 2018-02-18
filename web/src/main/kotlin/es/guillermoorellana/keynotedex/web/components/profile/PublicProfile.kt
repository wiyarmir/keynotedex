package es.guillermoorellana.keynotedex.web.components.profile

import es.guillermoorellana.keynotedex.web.components.submissions.*
import es.guillermoorellana.keynotedex.web.model.*
import react.*
import react.dom.*

class PublicProfile : RComponent<ProfileProps, RState>() {
    override fun RBuilder.render() {
        props.userProfile?.also {
            div("profile-container") {
                h1 { +it.user.displayName }
            }
            submissions { attrs { submissions = it.submissions } }
        }
    }
}

external interface ProfileProps : RProps {
    var userProfile: UserProfile?
}

fun RBuilder.publicProfile(handler: RHandler<ProfileProps>) = child(PublicProfile::class, handler)
