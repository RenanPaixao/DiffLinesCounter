package com.fappslab.linesdifftracker.domain.repository

import com.fappslab.linesdifftracker.domain.model.DiffStat

interface LinesDiffTrackerRepository {
    suspend fun query(basePath: String?, targetBranch: String? = null): DiffStat?
}
