package com.lab49.taptosnap.util

import org.junit.Assert
import org.junit.Test

class UtilsTest {
    @Test
    fun `test convert time millis to string format`() {
        val time = "00:02:00"
        Assert.assertEquals(formatTimeForTimer(Constants.TIMER_TASK), time)
    }
}