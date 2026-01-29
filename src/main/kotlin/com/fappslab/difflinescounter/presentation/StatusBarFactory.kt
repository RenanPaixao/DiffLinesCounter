package com.fappslab.difflinescounter.presentation

import com.fappslab.difflinescounter.data.repository.BranchConfigRepositoryImpl
import com.fappslab.difflinescounter.data.repository.DiffLinesCounterRepositoryImpl
import com.fappslab.difflinescounter.data.service.ProcessExecutorImpl
import com.fappslab.difflinescounter.data.source.DiffLinesCounterDataSourceCmdImpl
import com.fappslab.difflinescounter.data.source.GitBranchDataSource
import com.fappslab.difflinescounter.data.source.GitBranchDataSourceImpl
import com.fappslab.difflinescounter.data.storage.BranchMappingsState
import com.fappslab.difflinescounter.data.storage.PluginSettings
import com.fappslab.difflinescounter.domain.repository.BranchConfigRepository
import com.fappslab.difflinescounter.domain.repository.DiffLinesCounterRepository
import com.fappslab.difflinescounter.domain.usecase.GetBranchDiffUseCase
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

const val PLUGIN_NAME = "DiffLinesCounter"
const val ID = "com.fappslab.difflinescounter.presentation.StatusBarFactory"

class StatusBarFactory : StatusBarWidgetFactory {

    override fun getId(): String = ID

    override fun getDisplayName(): String = PLUGIN_NAME

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget {
        val executor = ProcessExecutorImpl()
        val diffRepository = provideDiffRepository(executor)
        val gitBranchDataSource = provideGitBranchDataSource(executor)
        val branchConfigRepository = provideBranchConfigRepository(project)

        val getBranchDiffUseCase = GetBranchDiffUseCase(
            diffRepository = diffRepository,
            configRepository = branchConfigRepository,
            gitBranchDataSource = gitBranchDataSource
        )

        return DiffStatusWidget(
            project = project,
            component = DiffStatusLabel(),
            getBranchDiffUseCase = getBranchDiffUseCase,
            gitBranchDataSource = gitBranchDataSource,
            branchConfigRepository = branchConfigRepository
        )
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true

    override fun isEnabledByDefault(): Boolean = true

    private fun provideDiffRepository(executor: ProcessExecutorImpl): DiffLinesCounterRepository {
        return DiffLinesCounterRepositoryImpl(
            dataSource = DiffLinesCounterDataSourceCmdImpl(executor = executor)
        )
    }

    private fun provideGitBranchDataSource(executor: ProcessExecutorImpl): GitBranchDataSource {
        return GitBranchDataSourceImpl(executor = executor)
    }

    private fun provideBranchConfigRepository(project: Project): BranchConfigRepository {
        return BranchConfigRepositoryImpl(
            pluginSettings = PluginSettings.getInstance(),
            branchMappingsState = BranchMappingsState.getInstance(project)
        )
    }
}
