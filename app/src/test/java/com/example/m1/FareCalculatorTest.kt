package com.example.m1

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for FareCalculator rules and concession logic
 */
class FareCalculatorTest {

    @Test
    fun testBaseFaresByDistance() {
        assertEquals(5.0, FareCalculator.calculateSingleFare(1), 0.01)
        assertEquals(5.0, FareCalculator.calculateSingleFare(3), 0.01)
        assertEquals(10.0, FareCalculator.calculateSingleFare(5), 0.01)
        assertEquals(15.0, FareCalculator.calculateSingleFare(8), 0.01)
        assertEquals(20.0, FareCalculator.calculateSingleFare(12), 0.01)
        assertEquals(25.0, FareCalculator.calculateSingleFare(18), 0.01)
        assertEquals(30.0, FareCalculator.calculateSingleFare(25), 0.01)
    }

    @Test
    fun testMultiplePassengers() {
        val total = FareCalculator.calculateTotalFare(stationCount = 5, passengers = 3)
        assertEquals(30.0, total, 0.01)
    }

    @Test
    fun testReturnJourneyDiscount() {
        // Base is 10.0 per ticket. Return = 10 * 2 * 0.9 = 18.0
        val total = FareCalculator.calculateTotalFare(stationCount = 5, passengers = 1, isReturnJourney = true)
        assertEquals(18.0, total, 0.01)
    }

    @Test
    fun testStudentConcession() {
        // Base 20.0, 20% discount = 16.0
        val total = FareCalculator.calculateTotalFare(stationCount = 12, passengers = 1, hasStudentConcession = true)
        assertEquals(16.0, total, 0.01)
    }
}
