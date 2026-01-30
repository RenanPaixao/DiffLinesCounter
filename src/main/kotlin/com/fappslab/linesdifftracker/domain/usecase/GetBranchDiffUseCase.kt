package com.fappslab.linesdifftracker.domain.usecase

import com.fappslab.linesdifftracker.data.source.GitBranchDataSource
import com.fappslab.linesdifftracker.data.source.GitTownDataSource
import com.fappslab.linesdifftracker.domain.model.BranchDiffResult
import com.fappslab.linesdifftracker.domain.model.DiffStat
import com.fappslab.linesdifftracker.domain.repository.BranchConfigRepository
import com.fappslab.linesdifftracker.domain.repository.LinesDiffTrackerRepository

class GetBranchDiffUseCase(
    private val diffRepository: LinesDiffTrackerRepository,
    private val configRepository: BranchConfigRepository,
    private val gitBranchDataSource: GitBranchDataSource,
    private val gitTownDataSource: GitTownDataSource
) {

    suspend operator fun invoke(basePath: String?): BranchDiffResult? {
        val currentBranch = gitBranchDataSource.getCurrentBranch(basePath)
            ?: return null // Detached HEAD or error

        val targetBranchName = resolveTargetBranch(basePath, currentBranch)
            ?: return null // No valid target branch found

        val (resolvedTarget, isLocalFallback, warning) = resolveRemoteOrLocal(basePath, targetBranchName)

        val diffStat = diffRepository.query(basePath, resolvedTarget)
            ?: return BranchDiffResult(
                diffStat = DiffStat(0, 0, 0),
                targetBranch = targetBranchName,
                isUsingLocalFallback = isLocalFallback,
                warningMessage = warning
            )

        return BranchDiffResult(
            diffStat = diffStat,
            targetBranch = targetBranchName,
            isUsingLocalFallback = isLocalFallback,
            warningMessage = warning
        )
    }

    private suspend fun resolveTargetBranch(basePath: String?, currentBranch: String): String? {
        // 1. Check per-branch mapping
        configRepository.getTargetBranchForSource(currentBranch)?.let { return it }

        // 2. Check Git Town parent (if enabled)
        if (configRepository.isGitTownEnabled()) {
            gitTownDataSource.getParentBranch(basePath)?.let { parentBranch ->
                if (branchExists(basePath, parentBranch)) return parentBranch
            }
        }

        // 3. Check default branch setting
        configRepository.getDefaultBranch()?.let { defaultBranch ->
            if (branchExists(basePath, defaultBranch)) return defaultBranch
        }

        // 4. Fallback chain: main -> master
        return FALLBACK_BRANCHES.firstOrNull { branchExists(basePath, it) }
    }

    private suspend fun branchExists(basePath: String?, branchName: String): Boolean {
        val branches = gitBranchDataSource.listAllBranches(basePath)
        val remoteName = gitBranchDataSource.getRemoteName(basePath)

        return branches.any { branch ->
            branch == branchName ||
                branch == "$remoteName/$branchName" ||
                branch.endsWith("/$branchName")
        }
    }

    private suspend fun resolveRemoteOrLocal(
        basePath: String?,
        branchName: String
    ): Triple<String, Boolean, String?> {
        val remoteName = gitBranchDataSource.getRemoteName(basePath)

        // Try remote first
        if (remoteName != null && gitBranchDataSource.hasRemoteBranch(basePath, branchName)) {
            return Triple("$remoteName/$branchName", false, null)
        }

        // Fall back to local
        return Triple(
            branchName,
            true,
            "Remote branch not available, using local '$branchName'"
        )
    }

    companion object {
        private val FALLBACK_BRANCHES = listOf("main", "master")
    }
}
