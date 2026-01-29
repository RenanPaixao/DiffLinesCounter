package com.fappslab.linesdifftracker.domain.usecase

import com.fappslab.linesdifftracker.domain.model.DiffStat
import com.fappslab.linesdifftracker.domain.repository.LinesDiffTrackerRepository

class GetDiffStatUseCase(
    private val repository: LinesDiffTrackerRepository
) {

    suspend operator fun invoke(basePath: String?): DiffStat? =
        repository.query(basePath)
}
