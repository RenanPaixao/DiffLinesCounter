package com.fappslab.linesdifftracker.domain.usecase

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class ScheduleUpdatesUseCaseTest {

    private val subject = ScheduleUpdatesUseCase()

    @Test
    fun `scheduleUpdates Should call scheduleBlock once When delay time has passed`() {
        runTest {
            // Given
            val delaySeconds = 1L
            val delayMillis = TimeUnit.SECONDS.toMillis(delaySeconds)
            val scheduleBlock: () -> Unit = mockk(relaxed = true)
            coEvery { scheduleBlock() } just Runs

            // When
            val job = launch { subject(delaySeconds, scheduleBlock) }
            advanceTimeBy(delayMillis)

            // Then
            coVerify(exactly = 1) { scheduleBlock() }
            job.cancel()
        }
    }
}
