package com.fappslab.linesdifftracker.stub

import com.fappslab.linesdifftracker.domain.model.BranchDiffResult
import com.fappslab.linesdifftracker.domain.model.BranchMapping
import com.fappslab.linesdifftracker.domain.model.DiffStat

fun diffStatStub(
    totalChanges: Int = 5,
    insertions: Int = 3,
    deletions: Int = 2
) = DiffStat(
    totalChanges = totalChanges,
    insertions = insertions,
    deletions = deletions
)

fun branchDiffResultStub(
    diffStat: DiffStat = diffStatStub(),
    targetBranch: String = "develop",
    isUsingLocalFallback: Boolean = false,
    warningMessage: String? = null
) = BranchDiffResult(
    diffStat = diffStat,
    targetBranch = targetBranch,
    isUsingLocalFallback = isUsingLocalFallback,
    warningMessage = warningMessage
)

fun branchMappingStub(
    sourceBranch: String = "feature/test",
    targetBranch: String = "develop"
) = BranchMapping(
    sourceBranch = sourceBranch,
    targetBranch = targetBranch
)

fun branchListStub() = listOf(
    "main",
    "develop",
    "feature/test",
    "origin/main",
    "origin/develop",
    "origin/feature/test"
)
