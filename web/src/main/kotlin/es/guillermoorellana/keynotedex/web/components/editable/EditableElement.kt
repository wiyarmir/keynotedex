package es.guillermoorellana.keynotedex.web.components.editable

import es.guillermoorellana.keynotedex.web.screens.inputValue
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import react.*
import react.dom.findDOMNode

abstract class EditableElement<T : HTMLElement> : RComponent<EditableElementProps, EditableElementState>() {

    override fun EditableElementState.init() {
        editing = false
        loading = false
        invalid = false
        disabled = false
    }

    protected open var refs: T? = null

    override fun componentWillReceiveProps(nextProps: EditableElementProps) {
        setState {
            editing = false
            loading = false
            invalid = false
            newValue = null
        }
    }

    override fun componentDidUpdate(prevProps: EditableElementProps, prevState: EditableElementState) {
        if (state.editing && !prevState.editing) {
            val element: T = findDOMNode(refs) as T
            element.focus()
            selectInputText(element)
        }
    }

    override fun RBuilder.render() {
        if (state.editing) {
            renderEditingComponent()
        } else {
            renderNormalComponent()
        }
    }

    protected abstract fun RBuilder.renderNormalComponent()

    protected abstract fun RBuilder.renderEditingComponent()

    protected open fun keyDown(event: Event) {
        when (event.keyCode) {
            KEY_ENTER -> finishEditing()
            KEY_ESCAPE -> cancelEditing()
        }
    }

    protected fun cancelEditing() {
        setState {
            editing = false
            invalid = false
        }
    }

    protected fun makeClassString(): String? =
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

    protected fun textChanged(event: Event) {
        this.doValidations(event.inputValue.trim())
    }

    protected fun startEditing() {
        setState {
            editing = true
        }
    }

    protected fun finishEditing() {
        val element: dynamic = findDOMNode(refs)
        val newValue: String = element.value
        doValidations(newValue)

        if (!state.invalid && props.value != newValue) {
            this.commit(newValue)
        }

        this.cancelEditing()
    }

    protected fun isDisabled() = props.shouldBlockWhileLoading != false && this.state.loading

    companion object {
        protected const val KEY_ENTER = 13
        protected const val KEY_ESCAPE = 27
        protected const val KEY_BACKSPACE = 8
    }
}

external interface EditableElementProps : RProps {
    var value: String
    var change: (event: ChangeEvent) -> Unit
    var propName: String
    var validate: ((value: String) -> Boolean)?
    var shouldBlockWhileLoading: Boolean?
    var className: String?
    var classEditing: String?
    var classLoading: String?
    var classInvalid: String?
    var classDisabled: String?
}

external interface EditableElementState : RState {
    var editing: Boolean
    var loading: Boolean
    var disabled: Boolean
    var invalid: Boolean
    var newValue: String?
}

external interface ChangeEvent {
    val propName: String
    val value: String
}

operator fun ChangeEvent.get(key: String) = if (key == propName) value else null

internal val Event.keyCode: Int
    get() = this.asDynamic().keyCode as? Int ?: 0
