package com.fappslab.linesdifftracker.data.repository

import com.fappslab.linesdifftracker.data.service.ProcessExecutor
import com.fappslab.linesdifftracker.data.source.LinesDiffTrackerDataSourceCmdImpl
import com.fappslab.linesdifftracker.stub.diffStatStub
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LinesDiffTrackerRepositoryImplIntegrationTest {

    private val executor = mockk<ProcessExecutor>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val subject = LinesDiffTrackerRepositoryImpl(
        LinesDiffTrackerDataSourceCmdImpl(executor, testDispatcher)
    )

    @Test
    fun `getDiffStat Should return DiffStat When invoke query`() {
        runTest {
            // Given
            val expectedDiffStat = diffStatStub()
            every { executor.run(any(), "git", "diff", "HEAD", "--stat") } returns getSuccessProcess()

            // When
            val result = subject.query(basePath = "/mock/basePath")

            // Then
            assertEquals(expectedDiffStat, result)
            verify { executor.run(any(), "git", "diff", "HEAD", "--stat") }
        }
    }

    @Test
    fun `getDiffStat Should return null When IOException occurs`() {
        runTest {
            // Given
            val expectedResult = null
            every { executor.run(any(), "git", "diff", "HEAD", "--stat") } returns getFailureProcess()

            // When
            val result = subject.query(basePath = "/mock/basePath")

            // Then
            assertEquals(expectedResult, result)
            verify { executor.run(any(), "git", "diff", "HEAD", "--stat") }
        }
    }
}
