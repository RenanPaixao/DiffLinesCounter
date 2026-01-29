package com.fappslab.linesdifftracker.data.model

import com.fappslab.linesdifftracker.stub.diffStatStub
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapperExtensionKtTest {

    @Test
    fun `toDiffStat Should return null When string is null`() {
        // Given
        val input: String? = null

        // When
        val result = input.toDiffStat()

        // Then
        assertNull(result)
    }

    @Test
    fun `toDiffStat Should return null When string does not match pattern`() {
        // Given
        val input = "This will not match"

        // When
        val result = input.toDiffStat()

        // Then
        assertNull(result)
    }

    @Test
    fun `toDiffStat Should return correct DiffStat When full match`() {
        // Given
        val input = "5 files changed, 3 insertions(+), 2 deletions(-)"
        val expectedResult = diffStatStub()

        // When
        val result = input.toDiffStat()

        // Then
        assertEquals(expectedResult, result)
    }
}
