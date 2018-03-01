package kotlinx.html

import kotlinx.html.attributes.StringAttribute
import react.RBuilder
import react.ReactElement
import react.dom.RDOMBuilder
import react.dom.tag

class MAIN(
    initialAttributes: Map<String, String>,
    override val consumer: TagConsumer<*>
) : HtmlBlockInlineTag, HTMLTag(
    "main",
    consumer,
    initialAttributes,
    null,
    false,
    false
) {
    var role: String
        get() = StringAttribute()[this, "role"]
        set(newValue) {
            StringAttribute()[this, "role"] = newValue
        }
}

inline fun RBuilder.main(classes: String? = null, block: RDOMBuilder<MAIN>.() -> Unit = {}): ReactElement =
    tag(block) { MAIN(attributesMapOf("class", classes), it) }
