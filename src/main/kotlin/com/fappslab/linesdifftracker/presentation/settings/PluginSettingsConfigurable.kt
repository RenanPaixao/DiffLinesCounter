package com.fappslab.linesdifftracker.presentation.settings

import com.fappslab.linesdifftracker.data.storage.PluginSettings
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class PluginSettingsConfigurable : Configurable {

    private var settingsPanel: PluginSettingsPanel? = null

    override fun getDisplayName(): String = "Diff Lines Counter"

    override fun createComponent(): JComponent {
        settingsPanel = PluginSettingsPanel()
        return settingsPanel!!.panel
    }

    override fun getPreferredFocusedComponent(): JComponent? =
        settingsPanel?.preferredFocusedComponent

    override fun isModified(): Boolean {
        val settings = PluginSettings.getInstance()
        return settingsPanel?.defaultBranch != (settings.defaultBranch ?: "") ||
            settingsPanel?.gitTownEnabled != settings.gitTownEnabled
    }

    override fun apply() {
        val settings = PluginSettings.getInstance()
        val newValue = settingsPanel?.defaultBranch
        settings.defaultBranch = if (newValue.isNullOrBlank()) null else newValue
        settings.gitTownEnabled = settingsPanel?.gitTownEnabled ?: false
    }

    override fun reset() {
        val settings = PluginSettings.getInstance()
        settingsPanel?.defaultBranch = settings.defaultBranch ?: ""
        settingsPanel?.gitTownEnabled = settings.gitTownEnabled
    }

    override fun disposeUIResources() {
        settingsPanel = null
    }
}
