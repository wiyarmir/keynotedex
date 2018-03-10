package es.guillermoorellana.keynotedex.web.components.profile

import es.guillermoorellana.keynotedex.web.components.editable.ChangeEvent
import es.guillermoorellana.keynotedex.web.components.editable.editableText
import es.guillermoorellana.keynotedex.web.components.editable.editableTextArea
import es.guillermoorellana.keynotedex.web.components.editable.get
import es.guillermoorellana.keynotedex.web.components.submissions.submissions
import es.guillermoorellana.keynotedex.web.model.User
import es.guillermoorellana.keynotedex.web.model.UserProfile
import react.*
import react.dom.div
import react.dom.style

//language=CSS
private const val profileStyle = """
.loading {
    color: grey;
    font-style: italic;
    cursor: wait;
}

.profile-container {
    padding: 40px 0;
    display: flex;
    flex-direction: column;
    align-items: stretch;
}

.displayNameInput {
    flex: 1;
    font-size: 2.5rem;
    text-align: center;
    word-wrap: break-word;
}

.bioTextArea {
    flex: 1;
    text-align: left;
}
"""

class EditableProfile : RComponent<EditableProfileProps, RState>() {

    override fun RBuilder.render() {
        style(content = profileStyle)
        props.userProfile.also { profile ->
            div("profile-container") {
                editableText {
                    attrs {
                        editable = props.editable
                        propName = "displayName"
                        value = profile.user.displayName
                        change = { event -> onChange(event) }
                        className = "displayNameInput"
                        classLoading = "loading"
                    }
                }
                editableTextArea {
                    attrs {
                        editable = props.editable
                        propName = "bio"
                        value = profile.user.bio ?: "Introduce yourself"
                        change = { event -> onChange(event) }
                        className = "bioTextArea"
                        classLoading = "loading"
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
        event["bio"]?.let { bio ->
            val user = props.userProfile.user
            onUserUpdated(user.copy(bio = bio))
        }
    }

    private fun onUserUpdated(newUser: User) {
        props.onUserProfileUpdated(props.userProfile.copy(user = newUser))
    }
}

external interface EditableProfileProps : RProps {
    var editable: Boolean
    var userProfile: UserProfile
    var onUserProfileUpdated: (UserProfile) -> Unit
}

fun RBuilder.editableProfile(handler: RHandler<EditableProfileProps>) = child(EditableProfile::class, handler)
