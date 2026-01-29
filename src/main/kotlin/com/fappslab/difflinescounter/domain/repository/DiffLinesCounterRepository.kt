package com.fappslab.difflinescounter.domain.repository

import com.fappslab.difflinescounter.domain.model.DiffStat

interface DiffLinesCounterRepository {
    suspend fun query(basePath: String?, targetBranch: String? = null): DiffStat?
}
