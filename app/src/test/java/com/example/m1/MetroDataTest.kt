package com.example.m1

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for MetroData stations, corridors, and fare calculation
 */
class MetroDataTest {

    @Test
    fun testAllStationsNotEmpty() {
        val stations = MetroData.allStations
        assertTrue(stations.isNotEmpty())
        assertTrue(stations.contains("Old High Court"))
        assertTrue(stations.contains("Thaltej Gam"))
        assertTrue(stations.contains("APMC"))
    }

    @Test
    fun testInterchangeStationLine() {
        val line = MetroData.getStationLine("Old High Court")
        assertTrue(line.contains("Interchange"))
    }

    @Test
    fun testFareCalculationDirectLine() {
        val fare = MetroData.calculateFare("Thaltej Gam", "Old High Court", 1)
        assertTrue(fare > 0.0)
        assertEquals(fare * 2, MetroData.calculateFare("Thaltej Gam", "Old High Court", 2), 0.01)
    }

    @Test
    fun testSameStationFare() {
        val fare = MetroData.calculateFare("Paldi", "Paldi", 1)
        assertEquals(5.0, fare, 0.01)
    }
}
