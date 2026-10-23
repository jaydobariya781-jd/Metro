package com.example.m1

import kotlin.math.abs

/**
 * Station and corridor metadata for Ahmedabad Metro Network
 */
object MetroData {

    val eastWestStations = listOf(
        "Thaltej Gam", "Thaltej", "Doordarshan Kendra", "Gurukul Road",
        "Gujarat University", "Commerce Six Road", "Stadium", "Old High Court",
        "Sabarmati River", "Shahpur", "Gheekanta", "Kalupur Railway Station",
        "Kankaria East", "Apparel Park", "Amraiwadi", "Rabari Colony",
        "Vastral", "Nirant Cross Road", "Vastral Gam"
    )

    val northSouthStations = listOf(
        "Motera Stadium", "Sabarmati", "AEC", "Sabarmati Railway Station",
        "Ranip", "Vadaj", "Vijay Nagar", "Usmanpura", "Old High Court",
        "Gandhigram", "Paldi", "Shreyas", "Rajiv Nagar", "Jivraj Park", "APMC"
    )

    val allStations: List<String> by lazy {
        (eastWestStations + northSouthStations).distinct().sorted()
    }

    fun getStationLine(station: String): String {
        val inEW = eastWestStations.contains(station)
        val inNS = northSouthStations.contains(station)
        return when {
            inEW && inNS -> "Interchange (Blue & Red Line)"
            inEW -> "East-West Corridor (Blue Line)"
            inNS -> "North-South Corridor (Red Line)"
            else -> "Ahmedabad Metro"
        }
    }

    fun calculateFare(from: String, to: String, passengers: Int = 1): Double {
        if (from.equals(to, ignoreCase = true)) {
            return 5.0 * passengers.coerceAtLeast(1)
        }

        val idx1EW = eastWestStations.indexOf(from)
        val idx2EW = eastWestStations.indexOf(to)

        val idx1NS = northSouthStations.indexOf(from)
        val idx2NS = northSouthStations.indexOf(to)

        val hops = when {
            idx1EW != -1 && idx2EW != -1 -> abs(idx1EW - idx2EW)
            idx1NS != -1 && idx2NS != -1 -> abs(idx1NS - idx2NS)
            else -> {
                // Cross line via Old High Court
                val hopsToInterchange = if (idx1EW != -1) {
                    abs(idx1EW - eastWestStations.indexOf("Old High Court"))
                } else {
                    abs(idx1NS - northSouthStations.indexOf("Old High Court"))
                }
                val hopsFromInterchange = if (idx2EW != -1) {
                    abs(idx2EW - eastWestStations.indexOf("Old High Court"))
                } else {
                    abs(idx2NS - northSouthStations.indexOf("Old High Court"))
                }
                hopsToInterchange + hopsFromInterchange
            }
        }

        return FareCalculator.calculateTotalFare(hops, passengers)
    }
}
