package com.fappslab.linesdifftracker.data.repository

import com.fappslab.linesdifftracker.data.source.LinesDiffTrackerDataSource
import com.fappslab.linesdifftracker.domain.model.DiffStat
import com.fappslab.linesdifftracker.domain.repository.LinesDiffTrackerRepository

class LinesDiffTrackerRepositoryImpl(
    private val dataSource: LinesDiffTrackerDataSource
) : LinesDiffTrackerRepository {

    override suspend fun query(basePath: String?, targetBranch: String?): DiffStat? =
        dataSource.query(basePath, targetBranch)
}
