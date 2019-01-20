package es.guillermoorellana.keynotedex.web.components.profile

import es.guillermoorellana.keynotedex.web.components.editable.ChangeEvent
import es.guillermoorellana.keynotedex.web.components.editable.editableText
import es.guillermoorellana.keynotedex.web.components.editable.editableTextArea
import es.guillermoorellana.keynotedex.web.components.editable.get
import es.guillermoorellana.keynotedex.web.components.submissions.sessions
import es.guillermoorellana.keynotedex.web.external.routeLink
import es.guillermoorellana.keynotedex.web.model.User
import es.guillermoorellana.keynotedex.web.model.UserProfile
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import react.dom.style

// language=CSS
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
        props.userProfile.also { (user, submissions) ->
            div("profile-container") {
                editableText {
                    attrs {
                        editable = props.editable
                        propName = "displayName"
                        value = user.displayName
                        change = { event -> onChange(event) }
                        className = "displayNameInput"
                        classLoading = "loading"
                    }
                }
                editableTextArea {
                    attrs {
                        editable = props.editable
                        propName = "bio"
                        value = user.bio ?: "Introduce yourself"
                        change = { event -> onChange(event) }
                        className = "bioTextArea"
                        classLoading = "loading"
                    }
                }
            }
            sessions { attrs { this.submissions = submissions } }
            if (props.editable) {
                routeLink(to = "/signout") {
                    attrs {
                        className = "btn btn-primary"
                    }
                    +"Log out"
                }
                routeLink(to = "/sessions/add") {
                    attrs {
                        className = "btn btn-primary"
                    }
                    +"New session"
                }
            }
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
