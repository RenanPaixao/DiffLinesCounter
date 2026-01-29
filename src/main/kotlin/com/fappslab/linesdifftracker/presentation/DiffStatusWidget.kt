package com.fappslab.linesdifftracker.presentation

import com.fappslab.linesdifftracker.data.source.GitBranchDataSource
import com.fappslab.linesdifftracker.domain.model.BranchDiffResult
import com.fappslab.linesdifftracker.domain.repository.BranchConfigRepository
import com.fappslab.linesdifftracker.domain.usecase.GetBranchDiffUseCase
import com.fappslab.linesdifftracker.presentation.action.GitBranchChangeListener
import com.fappslab.linesdifftracker.presentation.popup.TargetBranchPopup
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.BranchChangeListener
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.swing.JComponent

class DiffStatusWidget(
    private val project: Project,
    private val getBranchDiffUseCase: GetBranchDiffUseCase,
    private val gitBranchDataSource: GitBranchDataSource,
    private val branchConfigRepository: BranchConfigRepository
) : CustomStatusBarWidget {

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)
    private val connection = project.messageBus.connect(this)

    private val component = DiffStatusLabel(
        onRefreshClicked = ::refreshDiff,
        onTextClicked = ::onWidgetClicked
    )

    private var lastResult: BranchDiffResult? = null

    init {
        setupBranchChangeListener()
        setupFileSaveListener()
        refreshDiff()
    }

    override fun getComponent(): JComponent = component

    override fun ID(): String = ID

    override fun dispose() {
        connection.disconnect()
        job.cancel()
    }

    override fun install(statusBar: StatusBar) {}

    fun refreshDiff() {
        coroutineScope.launch {
            val result = getBranchDiffUseCase(project.basePath)
            lastResult = result
            component.showChanges(result)
        }
    }

    private fun setupBranchChangeListener() {
        connection.subscribe(
            BranchChangeListener.VCS_BRANCH_CHANGED,
            GitBranchChangeListener(::refreshDiff)
        )
    }

    private fun setupFileSaveListener() {
        connection.subscribe(
            FileDocumentManagerListener.TOPIC,
            object : FileDocumentManagerListener {
                override fun beforeDocumentSaving(document: Document) {
                    refreshDiff()
                }
            }
        )
    }

    private fun onWidgetClicked() {
        coroutineScope.launch {
            val currentBranch = gitBranchDataSource.getCurrentBranch(project.basePath)
            val branches = gitBranchDataSource.listAllBranches(project.basePath)
            val currentTarget = currentBranch?.let {
                branchConfigRepository.getTargetBranchForSource(it)
            } ?: branchConfigRepository.getDefaultBranch()

            launch(Dispatchers.Main) {
                showBranchPopup(currentBranch, currentTarget, branches)
            }
        }
    }

    private fun showBranchPopup(currentBranch: String?, currentTarget: String?, branches: List<String>) {
        val popup = TargetBranchPopup(
            currentTarget = currentTarget,
            branches = branches
        ) { selectedBranch ->
            onBranchSelected(currentBranch, selectedBranch)
        }

        popup.createPopup().showInCenterOf(component)
    }

    private fun onBranchSelected(currentBranch: String?, selectedBranch: String) {
        currentBranch?.let { source ->
            // Extract branch name if it's a remote branch (origin/branch -> branch)
            val targetName = if (selectedBranch.contains("/")) {
                selectedBranch.substringAfterLast("/")
            } else {
                selectedBranch
            }
            branchConfigRepository.setTargetBranchForSource(source, targetName)
            refreshDiff()
        }
    }
}
