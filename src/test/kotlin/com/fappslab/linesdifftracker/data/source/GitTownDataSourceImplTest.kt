package com.fappslab.linesdifftracker.data.source

import com.fappslab.linesdifftracker.data.service.ProcessExecutor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class GitTownDataSourceImplTest {

    private val executor = mockk<ProcessExecutor>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val subject = GitTownDataSourceImpl(executor, testDispatcher)

    @Test
    fun `getParentBranch Should return parent branch When git town returns valid parent`() = runTest {
        // Given
        val process = mockProcess("develop\n")
        every { executor.run(any(), "git", "town", "config", "get-parent") } returns process

        // When
        val result = subject.getParentBranch("/mock/path")

        // Then
        assertEquals("develop", result)
    }

    @Test
    fun `getParentBranch Should return null When git town is not installed`() = runTest {
        // Given
        every { executor.run(any(), "git", "town", "config", "get-parent") } throws RuntimeException("Process failed")

        // When
        val result = subject.getParentBranch("/mock/path")

        // Then
        assertNull(result)
    }

    @Test
    fun `getParentBranch Should return null When no parent is configured`() = runTest {
        // Given
        val process = mockProcess("") // Git Town returns empty output when no parent
        every { executor.run(any(), "git", "town", "config", "get-parent") } returns process

        // When
        val result = subject.getParentBranch("/mock/path")

        // Then
        assertNull(result)
    }

    @Test
    fun `getParentBranch Should trim whitespace from parent branch`() = runTest {
        // Given
        val process = mockProcess("  main  \n")
        every { executor.run(any(), "git", "town", "config", "get-parent") } returns process

        // When
        val result = subject.getParentBranch("/mock/path")

        // Then
        assertEquals("main", result)
    }

    @Test
    fun `getParentBranch Should return branch with slash in name`() = runTest {
        // Given
        val process = mockProcess("JIRAKEY-0000/some-random-name\n")
        every { executor.run(any(), "git", "town", "config", "get-parent") } returns process

        // When
        val result = subject.getParentBranch("/mock/path")

        // Then
        assertEquals("JIRAKEY-0000/some-random-name", result)
    }

    private fun mockProcess(output: String): Process {
        val process = mockk<Process>()
        every { process.inputStream } returns ByteArrayInputStream(output.toByteArray())
        return process
    }
}
