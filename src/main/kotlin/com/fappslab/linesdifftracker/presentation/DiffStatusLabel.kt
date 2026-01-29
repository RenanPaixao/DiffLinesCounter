package com.fappslab.linesdifftracker.presentation

import com.fappslab.linesdifftracker.domain.model.BranchDiffResult
import com.fappslab.linesdifftracker.domain.model.DiffStat
import com.fappslab.linesdifftracker.extension.orZero
import com.intellij.icons.AllIcons
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import java.awt.Cursor
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel

private const val TOOLTIP_FORMAT = "Target: %s | %d insertions(+), %d deletions(-)"
private const val TOOLTIP_WARNING_FORMAT = "%s\n\nWarning: %s"
private const val TEXT_FORMAT = "→%s: +%d:-%d"

class DiffStatusLabel(
    private val onRefreshClicked: () -> Unit = {},
    private val onTextClicked: () -> Unit = {}
) : JPanel(FlowLayout(FlowLayout.CENTER, 0, 0)) {

    private val textLabel = JLabel().apply {
        icon = AllIcons.Vcs.Branch
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                onTextClicked()
            }
        })
    }

    private val refreshButton = JLabel(AllIcons.Actions.Refresh).apply {
        toolTipText = "Refresh diff"
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        border = JBUI.Borders.emptyLeft(4)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                onRefreshClicked()
            }
        })
    }

    init {
        isOpaque = false
        add(textLabel)
        add(refreshButton)
        showChanges(null)
    }

    fun showChanges(result: BranchDiffResult?) {
        if (result == null) {
            showEmpty()
            return
        }

        val stat = result.diffStat.getOrZero()
        val targetBranch = result.targetBranch

        textLabel.text = formatText(targetBranch, stat)
        textLabel.toolTipText = formatTooltip(targetBranch, stat, result.warningMessage)

        textLabel.foreground = if (result.isUsingLocalFallback) {
            JBColor.YELLOW
        } else {
            JBColor.foreground()
        }
    }

    private fun showEmpty() {
        textLabel.text = "→-: +0:-0"
        textLabel.toolTipText = "No git repository detected"
        textLabel.foreground = JBColor.foreground()
    }

    private fun formatText(targetBranch: String, stat: DiffStat): String {
        return TEXT_FORMAT.format(targetBranch, stat.insertions.orZero(), stat.deletions.orZero())
    }

    private fun formatTooltip(targetBranch: String, stat: DiffStat, warning: String?): String {
        val baseTooltip = TOOLTIP_FORMAT.format(
            targetBranch,
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
