package com.fappslab.linesdifftracker.presentation

import com.fappslab.linesdifftracker.data.repository.BranchConfigRepositoryImpl
import com.fappslab.linesdifftracker.data.repository.LinesDiffTrackerRepositoryImpl
import com.fappslab.linesdifftracker.data.service.ProcessExecutorImpl
import com.fappslab.linesdifftracker.data.source.LinesDiffTrackerDataSourceCmdImpl
import com.fappslab.linesdifftracker.data.source.GitBranchDataSource
import com.fappslab.linesdifftracker.data.source.GitBranchDataSourceImpl
import com.fappslab.linesdifftracker.data.source.GitTownDataSource
import com.fappslab.linesdifftracker.data.source.GitTownDataSourceImpl
import com.fappslab.linesdifftracker.data.storage.BranchMappingsState
import com.fappslab.linesdifftracker.data.storage.PluginSettings
import com.fappslab.linesdifftracker.domain.repository.BranchConfigRepository
import com.fappslab.linesdifftracker.domain.repository.LinesDiffTrackerRepository
import com.fappslab.linesdifftracker.domain.usecase.GetBranchDiffUseCase
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

const val PLUGIN_NAME = "LinesDiffTracker"
const val ID = "com.fappslab.linesdifftracker.presentation.StatusBarFactory"

class StatusBarFactory : StatusBarWidgetFactory {

    override fun getId(): String = ID

    override fun getDisplayName(): String = PLUGIN_NAME

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget {
        val executor = ProcessExecutorImpl()
        val diffRepository = provideDiffRepository(executor)
        val gitBranchDataSource = provideGitBranchDataSource(executor)
        val gitTownDataSource = provideGitTownDataSource(executor)
        val branchConfigRepository = provideBranchConfigRepository(project)

        val getBranchDiffUseCase = GetBranchDiffUseCase(
            diffRepository = diffRepository,
            configRepository = branchConfigRepository,
            gitBranchDataSource = gitBranchDataSource,
            gitTownDataSource = gitTownDataSource
        )

        return DiffStatusWidget(
            project = project,
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

    private fun provideDiffRepository(executor: ProcessExecutorImpl): LinesDiffTrackerRepository {
        return LinesDiffTrackerRepositoryImpl(
            dataSource = LinesDiffTrackerDataSourceCmdImpl(executor = executor)
        )
    }

    private fun provideGitBranchDataSource(executor: ProcessExecutorImpl): GitBranchDataSource {
        return GitBranchDataSourceImpl(executor = executor)
    }

    private fun provideGitTownDataSource(executor: ProcessExecutorImpl): GitTownDataSource {
        return GitTownDataSourceImpl(executor = executor)
    }

    private fun provideBranchConfigRepository(project: Project): BranchConfigRepository {
        return BranchConfigRepositoryImpl(
            pluginSettings = PluginSettings.getInstance(),
            branchMappingsState = BranchMappingsState.getInstance(project)
        )
    }
}
