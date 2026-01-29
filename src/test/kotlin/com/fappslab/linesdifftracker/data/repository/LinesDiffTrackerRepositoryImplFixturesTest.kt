package com.fappslab.linesdifftracker.data.repository

import io.mockk.every
import io.mockk.mockk
import java.io.ByteArrayInputStream
import java.io.IOException

private const val RESULT = "5 files changed, 3 insertions(+), 2 deletions(-)"

fun getSuccessProcess(): Process =
    mockk<Process> { every { inputStream } returns ByteArrayInputStream(RESULT.toByteArray()) }

fun getFailureProcess(): Process =
    mockk<Process> { every { inputStream } throws IOException() }
