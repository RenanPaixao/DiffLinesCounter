package com.fappslab.linesdifftracker.presentation.action

import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class FileAction(private val onActionBlock: () -> Unit) : BulkFileListener {

    override fun after(events: MutableList<out VFileEvent>) {
        onActionBlock()
    }
}
