package com.fappslab.linesdifftracker.presentation.action

import com.intellij.openapi.vcs.BranchChangeListener

class GitBranchChangeListener(
    private val onBranchChanged: () -> Unit
) : BranchChangeListener {

    override fun branchWillChange(branchName: String) {
        // No action needed before branch change
    }

    override fun branchHasChanged(branchName: String) {
        onBranchChanged()
    }
}
