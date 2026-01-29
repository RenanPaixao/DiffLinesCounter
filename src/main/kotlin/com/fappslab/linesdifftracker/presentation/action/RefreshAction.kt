package com.fappslab.linesdifftracker.presentation.action

import com.fappslab.linesdifftracker.domain.model.ActionPlacesType
import com.fappslab.linesdifftracker.extension.refreshChangesActions
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager

class RefreshAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        ApplicationManager.getApplication().invokeLater {
            event.dataContext.refreshChangesActions(
                placeType = ActionPlacesType.KeyboardShortcut,
                inputEvent = event.inputEvent,
                modifiers = event.modifiers
            )
        }
    }
}
