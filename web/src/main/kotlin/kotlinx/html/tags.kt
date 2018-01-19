package kotlinx.html

import kotlinx.html.attributes.StringAttribute

class MAIN(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) : FlowContent, HTMLTag("main", consumer, initialAttributes, null, false, false) {
    var role: String
        get() = StringAttribute()[this, "role"]
        set(newValue) {
            StringAttribute()[this, "role"] = newValue
        }
}

fun <T, C : TagConsumer<T>> C.main(classes: String? = null, block: MAIN.() -> Unit = {}) = MAIN(attributesMapOf("class", classes), this).visit(block)
