package com.fappslab.linesdifftracker.data.source

interface GitBranchDataSource {
    suspend fun getCurrentBranch(basePath: String?): String?
    suspend fun listAllBranches(basePath: String?): List<String>
    suspend fun hasRemoteBranch(basePath: String?, branchName: String): Boolean
    suspend fun getRemoteName(basePath: String?): String?
}
