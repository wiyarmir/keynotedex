package es.guillermoorellana.keynotedex.web.components.profile

import es.guillermoorellana.keynotedex.web.components.submissions.*
import es.guillermoorellana.keynotedex.web.model.*
import react.*
import react.dom.*

class EditableProfile : RComponent<EditableProfileProps, RState>() {

    override fun RBuilder.render() {
        props.userProfile?.also {
            div("profile-container") {
                h1 { +it.user.displayName }
            }
            submissions { attrs { submissions = it.submissions } }
        }
    }
}

external interface EditableProfileProps : RProps {
    var userProfile: UserProfile?
}

fun RBuilder.editableProfile(handler: RHandler<EditableProfileProps>) = child(EditableProfile::class, handler)
