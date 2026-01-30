package com.fappslab.linesdifftracker.data.source

interface GitTownDataSource {
    suspend fun getParentBranch(basePath: String?): String?
}
