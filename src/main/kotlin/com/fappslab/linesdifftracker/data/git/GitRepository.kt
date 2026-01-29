package com.fappslab.linesdifftracker.data.git

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

object GitRepository {

    fun provider(basePath: String?): Repository? {
        return runCatching {
            val gitDir = File("$basePath/.git")
            require(gitDir.exists() && gitDir.isDirectory) { "Invalid Git directory" }

            val repository = FileRepositoryBuilder()
                .setGitDir(gitDir)
                .build()

            requireNotNull(repository.resolve("HEAD")) { "Invalid repository: HEAD not found." }
            repository
        }.getOrNull()
    }
}
