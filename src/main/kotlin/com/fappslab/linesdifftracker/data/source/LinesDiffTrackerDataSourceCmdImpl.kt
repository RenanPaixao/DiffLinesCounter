package com.fappslab.linesdifftracker.data.source

import com.fappslab.linesdifftracker.data.model.toDiffStat
import com.fappslab.linesdifftracker.data.service.ProcessExecutor
import com.fappslab.linesdifftracker.domain.model.DiffStat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LinesDiffTrackerDataSourceCmdImpl(
    private val executor: ProcessExecutor,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LinesDiffTrackerDataSource {

    override suspend fun query(basePath: String?, targetBranch: String?): DiffStat? {
        return withContext(dispatcher) {
            runCatching {
                val directory = basePath?.let(::File)
                val diffArgs = if (targetBranch != null) {
                    // Compare working tree (including uncommitted changes) against target branch
                    arrayOf("git", "diff", targetBranch, "--stat")
                } else {
                    arrayOf("git", "diff", "HEAD", "--stat")
                }
                val diffProcess = executor.run(directory, *diffArgs)

                val changes = diffProcess.inputStream.bufferedReader().useLines { it.lastOrNull() }
                changes.toDiffStat()
            }.getOrNull()
        }
    }
}
