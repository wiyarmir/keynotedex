package es.guillermoorellana.keynotedex.web.components.editable

import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onFocusFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.js.onKeyDownFunction
import kotlinx.html.tabIndex
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RHandler
import react.dom.br
import react.dom.defaultValue
import react.dom.span
import react.dom.textArea

class EditableTextArea : EditableElement<HTMLTextAreaElement>() {

    override fun RBuilder.renderNormalComponent() {
        val value = state.newValue ?: props.value

        span(makeClassString()) {
            attrs {
                tabIndex = "0"
                onClickFunction = { startEditing() }
                onFocusFunction = { startEditing() }
            }
            value.split('\n')
                .mapIndexed { index, line ->
                    val keyNumber = 2 * index
                    span {
                        key = keyNumber.toString()
                        +line
                    }
                    br { key = (keyNumber + 1).toString() }
                }
        }
    }

    override fun RBuilder.renderEditingComponent() {
        textArea(classes = makeClassString()) {
            attrs {
                defaultValue = props.value
                disabled = isDisabled()
                onInputFunction = { event: Event -> textChanged(event) }
                onBlurFunction = { finishEditing() }
                onKeyDownFunction = { event: Event -> keyDown(event) }
            }
            ref { refs = it as HTMLTextAreaElement? }
        }
    }

    override fun keyDown(event: Event) {
        when (event.keyCode) {
            KEY_ESCAPE -> cancelEditing()
        }
    }
}

fun RBuilder.editableTextArea(handler: RHandler<EditableElementProps>) = child(EditableTextArea::class, handler)
