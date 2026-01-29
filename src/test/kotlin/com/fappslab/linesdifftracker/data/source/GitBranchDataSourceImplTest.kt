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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GitBranchDataSourceImplTest {

    private val executor = mockk<ProcessExecutor>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val subject = GitBranchDataSourceImpl(executor, testDispatcher)

    @Test
    fun `getCurrentBranch Should return branch name When on a branch`() = runTest {
        // Given
        val process = mockProcess("feature/test\n")
        every { executor.run(any(), "git", "rev-parse", "--abbrev-ref", "HEAD") } returns process

        // When
        val result = subject.getCurrentBranch("/mock/path")

        // Then
        assertEquals("feature/test", result)
    }

    @Test
    fun `getCurrentBranch Should return null When in detached HEAD state`() = runTest {
        // Given
        val process = mockProcess("HEAD\n")
        every { executor.run(any(), "git", "rev-parse", "--abbrev-ref", "HEAD") } returns process

        // When
        val result = subject.getCurrentBranch("/mock/path")

        // Then
        assertNull(result)
    }

    @Test
    fun `listAllBranches Should return list of branches`() = runTest {
        // Given
        val branchOutput = "main\ndevelop\nfeature/test\norigin/main\norigin/develop"
        val process = mockProcess(branchOutput)
        every { executor.run(any(), "git", "branch", "-a", "--format=%(refname:short)") } returns process

        // When
        val result = subject.listAllBranches("/mock/path")

        // Then
        assertEquals(5, result.size)
        assertTrue(result.contains("main"))
        assertTrue(result.contains("origin/develop"))
    }

    @Test
    fun `listAllBranches Should return empty list When no branches`() = runTest {
        // Given
        val process = mockProcess("")
        every { executor.run(any(), "git", "branch", "-a", "--format=%(refname:short)") } returns process

        // When
        val result = subject.listAllBranches("/mock/path")

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `hasRemoteBranch Should return true When remote branch exists`() = runTest {
        // Given
        val remoteProcess = mockProcess("origin\n")
        every { executor.run(any(), "git", "remote") } returns remoteProcess

        val verifyProcess = mockk<Process>()
        every { verifyProcess.inputStream } returns ByteArrayInputStream(ByteArray(0))
        every { verifyProcess.waitFor() } returns 0
        every {
            executor.run(any(), "git", "show-ref", "--verify", "--quiet", "refs/remotes/origin/develop")
        } returns verifyProcess

        // When
        val result = subject.hasRemoteBranch("/mock/path", "develop")

        // Then
        assertTrue(result)
    }

    @Test
    fun `hasRemoteBranch Should return false When remote branch does not exist`() = runTest {
        // Given
        val remoteProcess = mockProcess("origin\n")
        every { executor.run(any(), "git", "remote") } returns remoteProcess

        val verifyProcess = mockk<Process>()
        every { verifyProcess.inputStream } returns ByteArrayInputStream(ByteArray(0))
        every { verifyProcess.waitFor() } returns 1
        every {
            executor.run(any(), "git", "show-ref", "--verify", "--quiet", "refs/remotes/origin/nonexistent")
        } returns verifyProcess

        // When
        val result = subject.hasRemoteBranch("/mock/path", "nonexistent")

        // Then
        assertFalse(result)
    }

    @Test
    fun `getRemoteName Should return origin When origin exists`() = runTest {
        // Given
        val process = mockProcess("origin\nupstream\n")
        every { executor.run(any(), "git", "remote") } returns process

        // When
        val result = subject.getRemoteName("/mock/path")

        // Then
        assertEquals("origin", result)
    }

    @Test
    fun `getRemoteName Should return first remote When origin does not exist`() = runTest {
        // Given
        val process = mockProcess("upstream\nfork\n")
        every { executor.run(any(), "git", "remote") } returns process

        // When
        val result = subject.getRemoteName("/mock/path")

        // Then
        assertEquals("upstream", result)
    }

    @Test
    fun `getRemoteName Should return null When no remotes configured`() = runTest {
        // Given
        val process = mockProcess("")
        every { executor.run(any(), "git", "remote") } returns process

        // When
        val result = subject.getRemoteName("/mock/path")

        // Then
        assertNull(result)
    }

    private fun mockProcess(output: String): Process {
        val process = mockk<Process>()
        every { process.inputStream } returns ByteArrayInputStream(output.toByteArray())
        return process
    }
}
