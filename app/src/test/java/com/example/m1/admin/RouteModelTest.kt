package com.example.m1.admin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for admin route model, frequency and corridor validation
 */
class RouteModelTest {

    @Test
    fun testRouteCreationAndAttributes() {
        val route = AdminRouteModel(
            routeCode = "RT-EW-01",
            sourceStation = "Vastral Gam",
            destinationStation = "Thaltej Gam",
            totalStations = 17,
            approximateTimeMinutes = 34,
            standardFare = 30,
            corridorType = "East-West Corridor"
        )

        assertEquals("RT-EW-01", route.routeCode)
        assertEquals("Vastral Gam", route.sourceStation)
        assertEquals("Thaltej Gam", route.destinationStation)
        assertEquals(30, route.standardFare)
        assertTrue(route.isActive)
        assertEquals(5, route.peakFrequencyMinutes)
    }

    @Test
    fun testCorridorTypeValidation() {
        val route = AdminRouteModel(
            routeCode = "RT-NS-01",
            sourceStation = "APMC",
            destinationStation = "Motera Stadium",
            totalStations = 15,
            approximateTimeMinutes = 30,
            standardFare = 25,
            corridorType = "North-South Corridor"
        )

        assertTrue(route.corridorType.startsWith("North-South"))
        assertEquals(15, route.totalStations)
    }
}
