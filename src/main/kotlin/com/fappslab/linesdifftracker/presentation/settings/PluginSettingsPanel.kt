package com.fappslab.linesdifftracker.presentation.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class PluginSettingsPanel {

    private val defaultBranchField = JBTextField()

    val panel: JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(JBLabel("Default target branch:"), defaultBranchField, 1, false)
        .addComponentFillVertically(JPanel(), 0)
        .panel

    val preferredFocusedComponent: JComponent
        get() = defaultBranchField

    var defaultBranch: String
        get() = defaultBranchField.text.trim()
        set(value) {
            defaultBranchField.text = value
        }
}
