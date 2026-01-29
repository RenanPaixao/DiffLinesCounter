package com.fappslab.difflinescounter.data.storage

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project

@State(
    name = "DiffLinesCounterBranchMappings",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
class BranchMappingsState : PersistentStateComponent<BranchMappingsState.State> {

    data class State(
        var mappings: MutableMap<String, String> = mutableMapOf()
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun getTargetBranch(sourceBranch: String): String? =
        state.mappings[sourceBranch]

    fun setTargetBranch(sourceBranch: String, targetBranch: String) {
        state.mappings[sourceBranch] = targetBranch
    }

    fun removeMapping(sourceBranch: String) {
        state.mappings.remove(sourceBranch)
    }

    fun getAllMappings(): Map<String, String> =
        state.mappings.toMap()

    companion object {
        fun getInstance(project: Project): BranchMappingsState =
            project.getService(BranchMappingsState::class.java)
    }
}
