package com.fappslab.linesdifftracker.data.source

import com.fappslab.linesdifftracker.domain.model.DiffStat

interface LinesDiffTrackerDataSource {
    suspend fun query(basePath: String?, targetBranch: String? = null): DiffStat?
}
