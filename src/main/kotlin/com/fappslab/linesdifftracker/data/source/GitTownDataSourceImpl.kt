package com.fappslab.linesdifftracker.data.source

import com.fappslab.linesdifftracker.data.service.ProcessExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class GitTownDataSourceImpl(
    private val executor: ProcessExecutor,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GitTownDataSource {

    override suspend fun getParentBranch(basePath: String?): String? {
        return withContext(dispatcher) {
            runCatching {
                val directory = basePath?.let(::File)
                val process = executor.run(directory, "git", "town", "config", "get-parent")
                val exitCode = process.waitFor()
                if (exitCode != 0) return@runCatching null

                val parent = process.inputStream.bufferedReader().readLine()?.trim()
                parent?.takeIf { it.isNotEmpty() }
            }.getOrNull()
        }
    }
}
