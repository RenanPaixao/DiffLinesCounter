package com.fappslab.difflinescounter.presentation

import com.fappslab.difflinescounter.domain.model.BranchDiffResult
import com.fappslab.difflinescounter.domain.model.DiffStat
import com.fappslab.difflinescounter.extension.orZero
import com.intellij.icons.AllIcons
import com.intellij.ui.JBColor
import javax.swing.JLabel

private const val TOOLTIP_FORMAT = "Target: %s | %d files changed, %d insertions(+), %d deletions(-)"
private const val TOOLTIP_WARNING_FORMAT = "%s\n\nWarning: %s"
private const val TEXT_FORMAT = "→%s: %d(+%d:-%d)"

class DiffStatusLabel : JLabel() {

    init {
        icon = AllIcons.Vcs.Branch
        showChanges(null)
    }

    fun showChanges(result: BranchDiffResult?) {
        if (result == null) {
            showEmpty()
            return
        }

        val stat = result.diffStat.getOrZero()
        val targetBranch = result.targetBranch

        text = formatText(targetBranch, stat)
        toolTipText = formatTooltip(targetBranch, stat, result.warningMessage)

        foreground = if (result.isUsingLocalFallback) {
            JBColor.YELLOW
        } else {
            JBColor.foreground()
        }
    }

    private fun showEmpty() {
        text = "→-: 0(+0:-0)"
        toolTipText = "No git repository detected"
        foreground = JBColor.foreground()
    }

    private fun formatText(targetBranch: String, stat: DiffStat): String {
        val totalChanges = stat.insertions.orZero() + stat.deletions.orZero()
        return TEXT_FORMAT.format(targetBranch, totalChanges, stat.insertions.orZero(), stat.deletions.orZero())
    }

    private fun formatTooltip(targetBranch: String, stat: DiffStat, warning: String?): String {
        val baseTooltip = TOOLTIP_FORMAT.format(
            targetBranch,
            stat.totalChanges.orZero(),
            stat.insertions.orZero(),
            stat.deletions.orZero()
        )

        return if (warning != null) {
            TOOLTIP_WARNING_FORMAT.format(baseTooltip, warning)
        } else {
            baseTooltip
        }
    }

    private fun DiffStat?.getOrZero(): DiffStat {
        return DiffStat(
            totalChanges = this?.totalChanges.orZero(),
            insertions = this?.insertions.orZero(),
            deletions = this?.deletions.orZero()
        )
    }
}
