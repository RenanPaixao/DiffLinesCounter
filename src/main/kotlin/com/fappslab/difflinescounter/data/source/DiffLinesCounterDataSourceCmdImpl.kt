package com.fappslab.difflinescounter.data.source

import com.fappslab.difflinescounter.data.model.toDiffStat
import com.fappslab.difflinescounter.data.service.ProcessExecutor
import com.fappslab.difflinescounter.domain.model.DiffStat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DiffLinesCounterDataSourceCmdImpl(
    private val executor: ProcessExecutor,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DiffLinesCounterDataSource {

    override suspend fun query(basePath: String?, targetBranch: String?): DiffStat? {
        return withContext(dispatcher) {
            runCatching {
                val directory = basePath?.let(::File)
                val diffArgs = if (targetBranch != null) {
                    arrayOf("git", "diff", "$targetBranch...HEAD", "--stat")
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
