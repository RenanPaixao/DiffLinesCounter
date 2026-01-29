package com.fappslab.linesdifftracker.domain.model

import com.intellij.openapi.actionSystem.ActionPlaces

sealed class ActionPlacesType(val place: String) {
    data object StatusBarPlace : ActionPlacesType(ActionPlaces.STATUS_BAR_PLACE)
    data object KeyboardShortcut : ActionPlacesType(ActionPlaces.KEYBOARD_SHORTCUT)
    data object Unknown : ActionPlacesType(ActionPlaces.UNKNOWN)
}
