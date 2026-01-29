package com.fappslab.linesdifftracker.data.storage

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "LinesDiffTrackerSettings",
    storages = [Storage("LinesDiffTrackerSettings.xml")]
)
class PluginSettings : PersistentStateComponent<PluginSettings.State> {

    data class State(
        var defaultBranch: String? = null
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    var defaultBranch: String?
        get() = state.defaultBranch
        set(value) {
            state.defaultBranch = value
        }

    companion object {
        fun getInstance(): PluginSettings =
            ApplicationManager.getApplication().getService(PluginSettings::class.java)
    }
}