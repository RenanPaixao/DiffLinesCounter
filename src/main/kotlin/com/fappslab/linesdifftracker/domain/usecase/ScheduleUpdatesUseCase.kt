package com.fappslab.linesdifftracker.domain.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

class ScheduleUpdatesUseCase {

    suspend operator fun invoke(delaySeconds: Long, scheduleBlock: () -> Unit) {
        while (coroutineContext.isActive) {
            scheduleBlock()
            delay(TimeUnit.SECONDS.toMillis(delaySeconds))
        }
    }
}
