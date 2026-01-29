package com.fappslab.difflinescounter.domain.model

data class TargetBranchConfig(
    val branchName: String,
    val isRemote: Boolean = true
)
