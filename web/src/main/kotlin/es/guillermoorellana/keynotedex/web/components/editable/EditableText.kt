package es.guillermoorellana.keynotedex.web.components.editable

import es.guillermoorellana.keynotedex.web.components.*
import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import react.*
import react.dom.*

class EditableText : RComponent<EditableTextProps, EditableTextState>() {
    companion object {
        private const val KEY_ENTER = 13
        private const val KEY_ESCAPE = 27
        private const val KEY_BACKSPACE = 8
    }

    private val refs: dynamic = js("{}")

    override fun EditableTextState.init(props: EditableTextProps) {
        editing = false
        loading = false
        disabled = false
        invalid = false
    }

    override fun componentWillReceiveProps(nextProps: EditableTextProps) {
        setState {
            editing = false
            loading = false
            invalid = false
            newValue = null
        }
    }

    override fun componentDidUpdate(prevProps: EditableTextProps, prevState: EditableTextState) {
        if (state.editing && !prevState.editing) {
            val inputElement: HTMLInputElement = findDOMNode(refs.input) as HTMLInputElement
            inputElement.focus()
            selectInputText(inputElement)
        }
    }

    override fun RBuilder.render() {
        if (state.editing) {
            renderEditingComponent()
        } else {
            renderNormalComponent()
        }
    }

    private fun makeClassString(): String? =
        mapOf(
            props.className to true,
            props.classDisabled to state.disabled,
            props.classEditing to state.editing,
            props.classInvalid to state.invalid,
            props.classLoading to state.loading
        )
            .filterKeys { it != null }
            .filterValues { it == true }
            .keys
            .joinToString(" ")

    private fun selectInputText(element: dynamic) {
        if (element.setSelectionRange !== undefined) {
            element.setSelectionRange(0, element.value.length)
        }
    }

    private fun commit(value: String) {
        if (state.invalid) return
        setState {
            loading = true
            newValue = value
        }

        props.change(
            object : ChangeEvent {
                override val propName = props.propName
                override val value = value
            }
        )
    }

    private fun doValidations(value: String) {
        if (props.validate == null) {
            setState {
                invalid = props.validate?.invoke(value) == false
            }
        }
    }

    private fun RBuilder.renderNormalComponent() {
        span(makeClassString()) {
            attrs {
                tabIndex = "0"
                onClickFunction = { startEditing() }
                onFocusFunction = { startEditing() }
            }
            +(state.newValue ?: props.value)
        }
    }

    private fun RBuilder.renderEditingComponent() {
        input(classes = makeClassString()) {
            attrs {
                defaultValue = props.value
                disabled = isDisabled()
                onInputFunction = this@EditableText::textChanged
                onBlurFunction = { finishEditing() }
                onKeyDownFunction = this@EditableText::keyDown
            }
            ref { refs.input = it }
        }
    }

    private fun keyDown(event: Event) {
        when (event.keyCode) {
            KEY_ENTER -> finishEditing()
            KEY_ESCAPE -> cancelEditing()
        }
    }

    private fun cancelEditing() {
        setState {
            editing = false
            invalid = false
        }
    }

    private fun textChanged(event: Event) {
        this.doValidations(event.inputValue.trim())
    }

    private fun startEditing() {
        setState {
            editing = true
        }
    }

    private fun finishEditing() {
        val inputElement: HTMLInputElement = findDOMNode(refs.input) as HTMLInputElement
        val newValue = inputElement.value
        doValidations(newValue)

        if (!state.invalid && props.value != newValue) {
            this.commit(newValue)
        }

        this.cancelEditing()
    }

    private fun isDisabled() = props.shouldBlockWhileLoading == true && this.state.loading
}

external interface EditableTextProps : RProps {
    var value: String
    var change: (chg: ChangeEvent) -> Unit
    var propName: String
    var validate: ((value: String) -> Boolean)?
    var shouldBlockWhileLoading: Boolean?
    var className: String?
    var classEditing: String?
    var classLoading: String?
    var classInvalid: String?
    var classDisabled: String?
}

external interface ChangeEvent {
    val propName: String
    val value: String
}

operator fun ChangeEvent.get(key: String) = if (key == propName) value else null

external interface EditableTextState : RState {
    var editing: Boolean
    var loading: Boolean
    var disabled: Boolean
    var invalid: Boolean
    var newValue: String?
}

fun RBuilder.editableText(handler: RHandler<EditableTextProps>) = child(EditableText::class, handler)

internal val Event.keyCode: Int
    get() = this.asDynamic().keyCode as? Int ?: 0
