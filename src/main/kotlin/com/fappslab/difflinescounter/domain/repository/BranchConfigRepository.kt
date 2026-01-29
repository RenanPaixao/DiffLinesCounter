package com.fappslab.difflinescounter.domain.repository

import com.fappslab.difflinescounter.domain.model.BranchMapping

interface BranchConfigRepository {
    fun getDefaultBranch(): String?
    fun setDefaultBranch(branch: String)
    fun getTargetBranchForSource(sourceBranch: String): String?
    fun setTargetBranchForSource(sourceBranch: String, targetBranch: String)
    fun getAllMappings(): List<BranchMapping>
    fun removeMapping(sourceBranch: String)
}
