package com.fappslab.linesdifftracker.domain.model

data class BranchDiffResult(
    val diffStat: DiffStat,
    val targetBranch: String,
    val isUsingLocalFallback: Boolean = false,
    val warningMessage: String? = null
)
