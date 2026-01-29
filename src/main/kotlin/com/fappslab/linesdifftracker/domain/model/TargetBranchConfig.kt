package com.fappslab.linesdifftracker.domain.model

data class TargetBranchConfig(
    val branchName: String,
    val isRemote: Boolean = true
)
