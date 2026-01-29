package com.fappslab.difflinescounter.data.source

import com.fappslab.difflinescounter.domain.model.DiffStat

interface DiffLinesCounterDataSource {
    suspend fun query(basePath: String?, targetBranch: String? = null): DiffStat?
}
