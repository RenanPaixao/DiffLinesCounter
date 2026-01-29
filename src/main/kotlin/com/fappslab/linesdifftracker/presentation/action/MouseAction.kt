package com.fappslab.linesdifftracker.presentation.action

import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class MouseAction(private val onActionBLock: () -> Unit) : MouseListener {

    override fun mouseClicked(e: MouseEvent?) {
        onActionBLock()
    }

    // :ISP:?
    override fun mousePressed(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
}
