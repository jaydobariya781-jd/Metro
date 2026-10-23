package com.example.m1

/**
 * Calculates Ahmedabad Metro ticketing fares based on station hops,
 * passenger count, journey type, and student/senior concessions.
 */
object FareCalculator {

    const val BASE_MINIMUM_FARE = 5.0
    const val RETURN_DISCOUNT_PERCENT = 0.10 // 10% discount on return journeys

    /**
     * Calculate fare based on number of stations travelled
     * 1-3 stations: ₹5
     * 4-6 stations: ₹10
     * 7-9 stations: ₹15
     * 10-14 stations: ₹20
     * 15-20 stations: ₹25
     * 20+ stations: ₹30
     */
    fun calculateSingleFare(stationCount: Int): Double {
        return when {
            stationCount <= 1 -> BASE_MINIMUM_FARE
            stationCount in 2..3 -> 5.0
            stationCount in 4..6 -> 10.0
            stationCount in 7..9 -> 15.0
            stationCount in 10..14 -> 20.0
            stationCount in 15..20 -> 25.0
            else -> 30.0
        }
    }

    /**
     * Calculates total fare for group and journey type
     */
    fun calculateTotalFare(
        stationCount: Int,
        passengers: Int,
        isReturnJourney: Boolean = false,
        hasStudentConcession: Boolean = false
    ): Double {
        val passengerCount = passengers.coerceAtLeast(1)
        val singleFare = calculateSingleFare(stationCount)
        var total = singleFare * passengerCount

        if (isReturnJourney) {
            val returnFare = singleFare * 2 * (1.0 - RETURN_DISCOUNT_PERCENT)
            total = returnFare * passengerCount
        }

        if (hasStudentConcession) {
            total *= 0.80 // 20% student discount
        }

        return Math.round(total * 100.0) / 100.0
    }
}
