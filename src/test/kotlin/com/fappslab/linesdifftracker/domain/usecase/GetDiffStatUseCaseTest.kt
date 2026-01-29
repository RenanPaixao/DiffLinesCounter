package com.fappslab.linesdifftracker.domain.usecase

import com.fappslab.linesdifftracker.domain.repository.LinesDiffTrackerRepository
import com.fappslab.linesdifftracker.stub.diffStatStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetDiffStatUseCaseTest {

    private val repository = mockk<LinesDiffTrackerRepository>()
    private val subject = GetDiffStatUseCase(repository)

    @Test
    fun `getDiffStat Should return DiffStat When invoke query`() {
        runTest {
            // Given
            val expectedResult = diffStatStub()
            coEvery { repository.query(any(), any()) } returns expectedResult

            // When
            val result = subject(basePath = "/mock/basePath")

            // Then
            assertEquals(expectedResult, result)
            coVerify { repository.query(any(), any()) }
        }
    }
}
