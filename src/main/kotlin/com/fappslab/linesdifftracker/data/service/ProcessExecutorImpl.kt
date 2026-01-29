package com.fappslab.linesdifftracker.data.service

import java.io.File

class ProcessExecutorImpl : ProcessExecutor {

    override fun run(directory: File?, vararg command: String): Process {
        return ProcessBuilder(*command)
            .directory(directory)
            .start()
    }
}
