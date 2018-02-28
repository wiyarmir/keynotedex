package es.guillermoorellana.keynotedex.web.components.profile

import es.guillermoorellana.keynotedex.web.components.editable.*
import es.guillermoorellana.keynotedex.web.components.submissions.*
import es.guillermoorellana.keynotedex.web.model.*
import react.*
import react.dom.*

//language=CSS
private const val profileStyle = """
.loading {
    color: grey;
    font-style: italic;
    cursor: wait;
}
"""

class EditableProfile : RComponent<EditableProfileProps, RState>() {

    override fun RBuilder.render() {
        style(content = profileStyle)
        props.userProfile.also { profile ->
            div("profile-container") {
                h1 {
                    editableText {
                        attrs {
                            propName = "displayName"
                            value = profile.user.displayName
                            change = { event -> onChange(event) }
                            classLoading = "loading"
                        }
                    }
                }
            }
            submissions { attrs { submissions = profile.submissions } }
        }
    }

    private fun onChange(event: ChangeEvent) {

        event["displayName"]?.let { displayName ->
            val user = props.userProfile.user
            onUserUpdated(user.copy(displayName = displayName))
        }
    }

    private fun onUserUpdated(newUser: User) {
        props.onUserProfileUpdated(props.userProfile.copy(user = newUser))
    }
}

external interface EditableProfileProps : RProps {
    var userProfile: UserProfile
    var onUserProfileUpdated: (UserProfile) -> Unit
}

fun RBuilder.editableProfile(handler: RHandler<EditableProfileProps>) = child(EditableProfile::class, handler)
