package com.fappslab.linesdifftracker.data.repository

import com.fappslab.linesdifftracker.data.storage.BranchMappingsState
import com.fappslab.linesdifftracker.data.storage.PluginSettings
import com.fappslab.linesdifftracker.domain.model.BranchMapping
import com.fappslab.linesdifftracker.domain.repository.BranchConfigRepository

class BranchConfigRepositoryImpl(
    private val pluginSettings: PluginSettings,
    private val branchMappingsState: BranchMappingsState
) : BranchConfigRepository {

    override fun getDefaultBranch(): String? =
        pluginSettings.defaultBranch

    override fun setDefaultBranch(branch: String) {
        pluginSettings.defaultBranch = branch
    }

    override fun getTargetBranchForSource(sourceBranch: String): String? =
        branchMappingsState.getTargetBranch(sourceBranch)

    override fun setTargetBranchForSource(sourceBranch: String, targetBranch: String) {
        branchMappingsState.setTargetBranch(sourceBranch, targetBranch)
    }

    override fun getAllMappings(): List<BranchMapping> =
        branchMappingsState.getAllMappings().map { (source, target) ->
            BranchMapping(sourceBranch = source, targetBranch = target)
        }

    override fun removeMapping(sourceBranch: String) {
        branchMappingsState.removeMapping(sourceBranch)
    }
}
