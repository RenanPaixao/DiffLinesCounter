package com.fappslab.linesdifftracker.domain.usecase

import com.fappslab.linesdifftracker.data.source.GitBranchDataSource
import com.fappslab.linesdifftracker.domain.repository.BranchConfigRepository
import com.fappslab.linesdifftracker.domain.repository.LinesDiffTrackerRepository
import com.fappslab.linesdifftracker.stub.diffStatStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetBranchDiffUseCaseTest {

    private val diffRepository = mockk<LinesDiffTrackerRepository>()
    private val configRepository = mockk<BranchConfigRepository>()
    private val gitBranchDataSource = mockk<GitBranchDataSource>()

    private val subject = GetBranchDiffUseCase(
        diffRepository = diffRepository,
        configRepository = configRepository,
        gitBranchDataSource = gitBranchDataSource
    )

    @Test
    fun `invoke Should return null When current branch is null (detached HEAD)`() = runTest {
        // Given
        coEvery { gitBranchDataSource.getCurrentBranch(any()) } returns null

        // When
        val result = subject("/mock/path")

        // Then
        assertNull(result)
    }

    @Test
    fun `invoke Should use per-branch mapping When mapping exists`() = runTest {
        // Given
        val currentBranch = "feature/test"
        val targetBranch = "develop"
        val expectedDiffStat = diffStatStub()

        coEvery { gitBranchDataSource.getCurrentBranch(any()) } returns currentBranch
        coEvery { configRepository.getTargetBranchForSource(currentBranch) } returns targetBranch
        coEvery { gitBranchDataSource.getRemoteName(any()) } returns "origin"
        coEvery { gitBranchDataSource.hasRemoteBranch(any(), targetBranch) } returns true
        coEvery { diffRepository.query(any(), "origin/$targetBranch") } returns expectedDiffStat

        // When
        val result = subject("/mock/path")

        // Then
        assertNotNull(result)
        assertEquals(targetBranch, result.targetBranch)
        assertEquals(expectedDiffStat, result.diffStat)
        assertEquals(false, result.isUsingLocalFallback)
    }

    @Test
    fun `invoke Should use default branch When no per-branch mapping exists`() = runTest {
        // Given
        val currentBranch = "feature/new"
        val defaultBranch = "main"
        val expectedDiffStat = diffStatStub()

        coEvery { gitBranchDataSource.getCurrentBranch(any()) } returns currentBranch
        coEvery { configRepository.getTargetBranchForSource(currentBranch) } returns null
        coEvery { configRepository.getDefaultBranch() } returns defaultBranch
        coEvery { gitBranchDataSource.listAllBranches(any()) } returns listOf("main", "origin/main")
        coEvery { gitBranchDataSource.getRemoteName(any()) } returns "origin"
        coEvery { gitBranchDataSource.hasRemoteBranch(any(), defaultBranch) } returns true
        coEvery { diffRepository.query(any(), "origin/$defaultBranch") } returns expectedDiffStat

        // When
        val result = subject("/mock/path")

        // Then
        assertNotNull(result)
        assertEquals(defaultBranch, result.targetBranch)
    }

    @Test
    fun `invoke Should fallback to main When no mapping and no default configured`() = runTest {
        // Given
        val currentBranch = "feature/test"
        val expectedDiffStat = diffStatStub()

        coEvery { gitBranchDataSource.getCurrentBranch(any()) } returns currentBranch
        coEvery { configRepository.getTargetBranchForSource(currentBranch) } returns null
        coEvery { configRepository.getDefaultBranch() } returns null
        coEvery { gitBranchDataSource.listAllBranches(any()) } returns listOf("main", "origin/main")
        coEvery { gitBranchDataSource.getRemoteName(any()) } returns "origin"
        coEvery { gitBranchDataSource.hasRemoteBranch(any(), "main") } returns true
        coEvery { diffRepository.query(any(), "origin/main") } returns expectedDiffStat

        // When
        val result = subject("/mock/path")

        // Then
        assertNotNull(result)
        assertEquals("main", result.targetBranch)
    }

    @Test
    fun `invoke Should fallback to master When main does not exist`() = runTest {
        // Given
        val currentBranch = "feature/test"
        val expectedDiffStat = diffStatStub()

        coEvery { gitBranchDataSource.getCurrentBranch(any()) } returns currentBranch
        coEvery { configRepository.getTargetBranchForSource(currentBranch) } returns null
        coEvery { configRepository.getDefaultBranch() } returns null
        coEvery { gitBranchDataSource.listAllBranches(any()) } returns listOf("master", "origin/master")
        coEvery { gitBranchDataSource.getRemoteName(any()) } returns "origin"
        coEvery { gitBranchDataSource.hasRemoteBranch(any(), "master") } returns true
        coEvery { diffRepository.query(any(), "origin/master") } returns expectedDiffStat

        // When
        val result = subject("/mock/path")

        // Then
        assertNotNull(result)
        assertEquals("master", result.targetBranch)
    }

    @Test
    fun `invoke Should use local branch with warning When remote not available`() = runTest {
        // Given
        val currentBranch = "feature/test"
        val targetBranch = "develop"
        val expectedDiffStat = diffStatStub()

        coEvery { gitBranchDataSource.getCurrentBranch(any()) } returns currentBranch
        coEvery { configRepository.getTargetBranchForSource(currentBranch) } returns targetBranch
        coEvery { gitBranchDataSource.getRemoteName(any()) } returns "origin"
        coEvery { gitBranchDataSource.hasRemoteBranch(any(), targetBranch) } returns false
        coEvery { diffRepository.query(any(), targetBranch) } returns expectedDiffStat

        // When
        val result = subject("/mock/path")

        // Then
        assertNotNull(result)
        assertEquals(targetBranch, result.targetBranch)
        assertTrue(result.isUsingLocalFallback)
        assertNotNull(result.warningMessage)
    }

    @Test
    fun `invoke Should use local branch When no remote configured`() = runTest {
        // Given
        val currentBranch = "feature/test"
        val targetBranch = "develop"
        val expectedDiffStat = diffStatStub()

        coEvery { gitBranchDataSource.getCurrentBranch(any()) } returns currentBranch
        coEvery { configRepository.getTargetBranchForSource(currentBranch) } returns targetBranch
        coEvery { gitBranchDataSource.getRemoteName(any()) } returns null
        coEvery { diffRepository.query(any(), targetBranch) } returns expectedDiffStat

        // When
        val result = subject("/mock/path")

        // Then
        assertNotNull(result)
        assertTrue(result.isUsingLocalFallback)
    }

    @Test
    fun `invoke Should return zero diff When repository query returns null`() = runTest {
        // Given
        val currentBranch = "feature/test"
        val targetBranch = "develop"

        coEvery { gitBranchDataSource.getCurrentBranch(any()) } returns currentBranch
        coEvery { configRepository.getTargetBranchForSource(currentBranch) } returns targetBranch
        coEvery { gitBranchDataSource.getRemoteName(any()) } returns "origin"
        coEvery { gitBranchDataSource.hasRemoteBranch(any(), targetBranch) } returns true
        coEvery { diffRepository.query(any(), any()) } returns null

        // When
        val result = subject("/mock/path")

        // Then
        assertNotNull(result)
        assertEquals(0, result.diffStat.totalChanges)
        assertEquals(0, result.diffStat.insertions)
        assertEquals(0, result.diffStat.deletions)
    }
}
