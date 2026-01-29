package com.fappslab.difflinescounter.data.repository

import com.fappslab.difflinescounter.data.source.DiffLinesCounterDataSource
import com.fappslab.difflinescounter.domain.model.DiffStat
import com.fappslab.difflinescounter.domain.repository.DiffLinesCounterRepository

class DiffLinesCounterRepositoryImpl(
    private val dataSource: DiffLinesCounterDataSource
) : DiffLinesCounterRepository {

    override suspend fun query(basePath: String?, targetBranch: String?): DiffStat? =
        dataSource.query(basePath)
}
