package com.fappslab.linesdifftracker.data.service

import java.io.File

interface ProcessExecutor {
    fun run(directory: File?, vararg command: String): Process
}
