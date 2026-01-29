package com.fappslab.linesdifftracker.data.source

import com.fappslab.linesdifftracker.data.service.ProcessExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class GitBranchDataSourceImpl(
    private val executor: ProcessExecutor,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GitBranchDataSource {

    override suspend fun getCurrentBranch(basePath: String?): String? {
        return withContext(dispatcher) {
            runCatching {
                val directory = basePath?.let(::File)
                val process = executor.run(directory, "git", "rev-parse", "--abbrev-ref", "HEAD")
                val branch = process.inputStream.bufferedReader().readLine()?.trim()
                // Returns "HEAD" when in detached HEAD state
                if (branch == "HEAD") null else branch
            }.getOrNull()
        }
    }

    override suspend fun listAllBranches(basePath: String?): List<String> {
        return withContext(dispatcher) {
            runCatching {
                val directory = basePath?.let(::File)
                val process = executor.run(directory, "git", "branch", "-a", "--format=%(refname:short)")
                process.inputStream.bufferedReader().readLines()
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .distinct()
            }.getOrDefault(emptyList())
        }
    }

    override suspend fun hasRemoteBranch(basePath: String?, branchName: String): Boolean {
        return withContext(dispatcher) {
            runCatching {
                val directory = basePath?.let(::File)
                val remoteName = getRemoteNameInternal(directory) ?: return@runCatching false
                val remoteBranch = "$remoteName/$branchName"

                val process = executor.run(
                    directory,
                    "git", "show-ref", "--verify", "--quiet", "refs/remotes/$remoteBranch"
                )
                process.waitFor() == 0
            }.getOrDefault(false)
        }
    }

    override suspend fun getRemoteName(basePath: String?): String? {
        return withContext(dispatcher) {
            val directory = basePath?.let(::File)
            getRemoteNameInternal(directory)
        }
    }

    private fun getRemoteNameInternal(directory: File?): String? {
        return runCatching {
            val process = executor.run(directory, "git", "remote")
            val remotes = process.inputStream.bufferedReader().readLines()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
            remotes.find { it == "origin" } ?: remotes.firstOrNull()
        }.getOrNull()
    }
}
