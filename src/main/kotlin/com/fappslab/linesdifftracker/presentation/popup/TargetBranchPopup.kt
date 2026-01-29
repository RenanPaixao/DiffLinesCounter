package com.fappslab.linesdifftracker.presentation.popup

import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.speedSearch.SpeedSearchUtil
import javax.swing.JList
import javax.swing.ListSelectionModel

class TargetBranchPopup(
    private val currentTarget: String?,
    private val branches: List<String>,
    private val onBranchSelected: (String) -> Unit
) {

    fun createPopup(): JBPopup {
        return JBPopupFactory.getInstance()
            .createPopupChooserBuilder(branches)
            .setTitle(buildTitle())
            .setMovable(true)
            .setResizable(true)
            .setRequestFocus(true)
            .setNamerForFiltering { it }
            .setRenderer(BranchListCellRenderer(currentTarget))
            .setItemChosenCallback { selected ->
                onBranchSelected(selected)
            }
            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
            .createPopup()
    }

    private fun buildTitle(): String {
        return if (currentTarget != null) {
            "Target: $currentTarget"
        } else {
            "Select Target Branch"
        }
    }

    private class BranchListCellRenderer(
        private val currentTarget: String?
    ) : ColoredListCellRenderer<String>() {

        override fun customizeCellRenderer(
            list: JList<out String>,
            value: String?,
            index: Int,
            selected: Boolean,
            hasFocus: Boolean
        ) {
            value ?: return

            val isCurrentTarget = value == currentTarget || value.endsWith("/$currentTarget")
            val isRemote = value.contains("/")

            icon = null

            if (isCurrentTarget) {
                append(value, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES)
                append(" (current)", SimpleTextAttributes.GRAYED_ATTRIBUTES)
            } else if (isRemote) {
                append(value, SimpleTextAttributes.REGULAR_ATTRIBUTES)
            } else {
                append(value, SimpleTextAttributes.REGULAR_ITALIC_ATTRIBUTES)
            }

            SpeedSearchUtil.applySpeedSearchHighlighting(list, this, true, selected)
        }
    }
}
