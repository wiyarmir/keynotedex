package es.guillermoorellana.keynotedex.backend.index

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.*

class KeynotedexPageContent : Template<HTML> {
    val head = Placeholder<HEAD>()
    val bundle = Placeholder<SCRIPT>()

    override fun HTML.apply() {
        head {
            meta { charset = "utf-8" }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1.0, shrink-to-fit=no"
            }
            title {
                +"Keynotedex"
            }
            insert(head)
            link(
                rel = LinkRel.stylesheet,
                type = LinkType.textCss,
                href = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
            ) {
                attributes["integrity"] = "sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
                attributes["crossorigin"] = "anonymous"
            }
        }
        body {
            div { id = "content" }

            script(src = "https://code.jquery.com/jquery-3.2.1.slim.min.js") {
                attributes["integrity"] = "sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
                attributes["crossorigin"] = "anonymous"
            }
            script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js") {
                attributes["integrity"] = "sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
                attributes["crossorigin"] = "anonymous"
            }
            script(src = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js") {
                attributes["integrity"] = "sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
                attributes["crossorigin"] = "anonymous"
            }

            script { insert(bundle) }
        }
    }
}
