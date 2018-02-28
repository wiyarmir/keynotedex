package es.guillermoorellana.keynotedex.web.components.editable

import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import react.*
import react.dom.*

open class EditableText : EditableElement<HTMLInputElement>() {

    override fun RBuilder.renderNormalComponent() {
        span(makeClassString()) {
            attrs {
                tabIndex = "0"
                onClickFunction = { startEditing() }
                onFocusFunction = { startEditing() }
            }
            +(state.newValue ?: props.value)
        }
    }

    override fun RBuilder.renderEditingComponent() {
        input(classes = makeClassString()) {
            attrs {
                defaultValue = props.value
                disabled = isDisabled()
                onInputFunction = { event: Event -> textChanged(event) }
                onBlurFunction = { finishEditing() }
                onKeyDownFunction = { event: Event -> keyDown(event) }
            }
            ref { refs = it as HTMLInputElement? }
        }
    }

}

fun RBuilder.editableText(handler: RHandler<EditableElementProps>) = child(EditableText::class, handler)
