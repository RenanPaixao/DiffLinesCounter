package com.fappslab.linesdifftracker.data.model

import com.fappslab.linesdifftracker.domain.model.DiffStat

private const val CHANGES_PATTERN =
    """(\d+)\s+files? changed(?:,\s+(\d+)\s+insertions?\(\+\))?(?:,\s+(\d+)\s+deletions?\(-\))?"""

fun String?.toDiffStat(): DiffStat? {
    return this?.trim()?.let {
        val matchResult = CHANGES_PATTERN.toRegex().find(it)
        val (totalChanges, insertions, deletions) = matchResult?.destructured ?: return null
        DiffStat(totalChanges.toIntOrNull(), insertions.toIntOrNull(), deletions.toIntOrNull())
    }
}
