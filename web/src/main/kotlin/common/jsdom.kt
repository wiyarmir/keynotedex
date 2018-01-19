package org.jetbrains.common

import org.w3c.dom.*
import org.w3c.dom.events.Event

internal val Event.inputValue: String
    get() = (target as? HTMLInputElement)?.value ?: (target as? HTMLTextAreaElement)?.value ?: ""

