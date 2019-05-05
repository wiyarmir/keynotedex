package es.guillermoorellana.keynotedex.web.screens

import es.guillermoorellana.keynotedex.repository.model.SessionPreview
import es.guillermoorellana.keynotedex.web.components.signUpPromo
import es.guillermoorellana.keynotedex.web.components.submissions.sessionPreview
import es.guillermoorellana.keynotedex.web.context.UserContext
import kotlinx.html.id
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.style

class HomeScreen : RComponent<RProps, RState>() {

    private val previews = listOf(preview(), preview(), preview())

    override fun RBuilder.render() {
        div {
            UserContext.Consumer { user ->
                if (user == null) signUpPromo {}
                else style { +"#recent-submissions{ margin-top: 2rem; }" }
            }
            div("container") {
                attrs { id = "recent-submissions" }
                div("row") {
                    previews.forEach {
                        sessionPreview { attrs { preview = it } }
                    }
                }
            }
        }
    }
}

private fun preview() = SessionPreview(
    userId = "user1",
    sessionId = "1",
    title = "Subheading",
    userDisplayName = "User 1"
)
