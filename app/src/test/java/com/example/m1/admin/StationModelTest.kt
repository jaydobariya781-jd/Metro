package com.example.m1.admin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for admin station model and line attributes
 */
class StationModelTest {

    @Test
    fun testStationCreationAndDefaults() {
        val station = AdminStationModel(
            id = "ST01",
            name = "Vastral Gam",
            lineName = "East-West (Blue)",
            lineColor = "#2563EB",
            platforms = 2,
            isInterchange = false
        )

        assertEquals("ST01", station.id)
        assertEquals("Vastral Gam", station.name)
        assertTrue(station.isOperational)
        assertEquals(2, station.platforms)
    }

    @Test
    fun testInterchangeStationAttributes() {
        val interchange = AdminStationModel(
            id = "ST08",
            name = "Old High Court",
            lineName = "Interchange",
            lineColor = "#10B981",
            platforms = 4,
            isInterchange = true
        )

        assertTrue(interchange.isInterchange)
        assertEquals(4, interchange.platforms)
        assertNotNull(interchange.firstTrainTime)
    }
}
