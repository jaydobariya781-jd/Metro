package com.example.m1

import android.content.Context
import org.json.JSONArray
import kotlin.math.abs

/**
 * Station and corridor metadata for Ahmedabad Metro Network.
 * Supports persistent storage so admin updates remain after restarting app.
 */
object MetroData {

    private const val PREFS_NAME = "metro_stations_db"
    private const val KEY_EW_STATIONS = "east_west_stations"
    private const val KEY_NS_STATIONS = "north_south_stations"

    // Default East-West Corridor (Blue Line) stations
    val defaultEastWestStations = listOf(
        "Thaltej Gam", "Thaltej", "Doordarshan Kendra", "Gurukul Road",
        "Gujarat University", "Commerce Six Road", "Stadium", "Old High Court",
        "Sabarmati River", "Shahpur", "Gheekanta", "Kalupur Railway Station",
        "Kankaria East", "Apparel Park", "Amraiwadi", "Rabari Colony",
        "Vastral", "Nirant Cross Road", "Vastral Gam"
    )

    // Default North-South Corridor (Red Line) stations
    val defaultNorthSouthStations = listOf(
        "Motera Stadium", "Sabarmati", "AEC", "Sabarmati Railway Station",
        "Ranip", "Vadaj", "Vijay Nagar", "Usmanpura", "Old High Court",
        "Gandhigram", "Paldi", "Shreyas", "Rajiv Nagar", "Jivraj Park", "APMC"
    )

    val eastWestStations: MutableList<String> = defaultEastWestStations.toMutableList()
    val northSouthStations: MutableList<String> = defaultNorthSouthStations.toMutableList()

    private var isInitialized = false

    val allStations: List<String>
        get() = (eastWestStations + northSouthStations).distinct().sorted()

    fun getAllStations(context: Context? = null): List<String> {
        context?.let { loadStations(it) }
        return allStations
    }

    @Synchronized
    fun loadStations(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val ewJson = prefs.getString(KEY_EW_STATIONS, null)
        val nsJson = prefs.getString(KEY_NS_STATIONS, null)

        if (ewJson != null) {
            try {
                val array = JSONArray(ewJson)
                val list = mutableListOf<String>()
                for (i in 0 until array.length()) {
                    list.add(array.getString(i))
                }
                if (list.isNotEmpty()) {
                    eastWestStations.clear()
                    eastWestStations.addAll(list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (nsJson != null) {
            try {
                val array = JSONArray(nsJson)
                val list = mutableListOf<String>()
                for (i in 0 until array.length()) {
                    list.add(array.getString(i))
                }
                if (list.isNotEmpty()) {
                    northSouthStations.clear()
                    northSouthStations.addAll(list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        isInitialized = true
    }

    @Synchronized
    fun saveStations(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val ewArray = JSONArray(eastWestStations)
        val nsArray = JSONArray(northSouthStations)

        prefs.edit()
            .putString(KEY_EW_STATIONS, ewArray.toString())
            .putString(KEY_NS_STATIONS, nsArray.toString())
            .apply()
    }

    /**
     * Add a new station to the network.
     * Prevents duplicate station names.
     */
    @Synchronized
    fun addStation(context: Context, stationName: String, line: String): Boolean {
        loadStations(context)
        val trimmed = stationName.trim()
        if (trimmed.isEmpty()) return false

        // Check duplicates across all stations
        if (allStations.any { it.equals(trimmed, ignoreCase = true) }) {
            return false
        }

        val isRedLine = line.contains("Red", ignoreCase = true) || line.contains("North", ignoreCase = true)
        if (isRedLine) {
            northSouthStations.add(trimmed)
        } else {
            eastWestStations.add(trimmed)
        }

        saveStations(context)

        // Also sync with StationRepository
        try {
            val lineName = if (isRedLine) "North-South (Red)" else "East-West (Blue)"
            com.example.m1.admin.StationRepository.getInstance(context).addStation(trimmed, lineName)
        } catch (ignored: Exception) {}

        return true
    }

    /**
     * Rename an existing station everywhere in the network.
     * Keeps its order and index for accurate hop / fare calculation.
     */
    @Synchronized
    fun renameStation(context: Context, oldName: String, newName: String): Boolean {
        loadStations(context)
        val trimmedOld = oldName.trim()
        val trimmedNew = newName.trim()

        if (trimmedNew.isEmpty()) return false
        if (trimmedOld.equals(trimmedNew, ignoreCase = true)) return false

        // Ensure new name does not conflict with existing different station
        if (allStations.any { !it.equals(trimmedOld, ignoreCase = true) && it.equals(trimmedNew, ignoreCase = true) }) {
            return false
        }

        var changed = false

        // Update in East-West Line
        val idxEW = eastWestStations.indexOfFirst { it.equals(trimmedOld, ignoreCase = true) }
        if (idxEW != -1) {
            eastWestStations[idxEW] = trimmedNew
            changed = true
        }

        // Update in North-South Line
        val idxNS = northSouthStations.indexOfFirst { it.equals(trimmedOld, ignoreCase = true) }
        if (idxNS != -1) {
            northSouthStations[idxNS] = trimmedNew
            changed = true
        }

        if (changed) {
            saveStations(context)
            try {
                com.example.m1.admin.StationRepository.getInstance(context).renameStation(trimmedOld, trimmedNew)
            } catch (ignored: Exception) {}
        }

        return changed
    }

    fun getStationLine(station: String): String {
        val inEW = eastWestStations.any { it.equals(station, ignoreCase = true) }
        val inNS = northSouthStations.any { it.equals(station, ignoreCase = true) }
        return when {
            inEW && inNS -> "Interchange (Blue & Red Line)"
            inEW -> "East-West Corridor (Blue Line)"
            inNS -> "North-South Corridor (Red Line)"
            else -> "Ahmedabad Metro"
        }
    }

    fun calculateFare(from: String, to: String, passengers: Int = 1, isReturn: Boolean = false): Double {
        val passengerCount = passengers.coerceAtLeast(1)
        if (from.equals(to, ignoreCase = true)) {
            val minFare = 5.0 * passengerCount
            return if (isReturn) minFare * 2 * (1.0 - FareCalculator.RETURN_DISCOUNT_PERCENT) else minFare
        }

        val idx1EW = eastWestStations.indexOfFirst { it.equals(from, ignoreCase = true) }
        val idx2EW = eastWestStations.indexOfFirst { it.equals(to, ignoreCase = true) }

        val idx1NS = northSouthStations.indexOfFirst { it.equals(from, ignoreCase = true) }
        val idx2NS = northSouthStations.indexOfFirst { it.equals(to, ignoreCase = true) }

        val hops = when {
            idx1EW != -1 && idx2EW != -1 -> abs(idx1EW - idx2EW)
            idx1NS != -1 && idx2NS != -1 -> abs(idx1NS - idx2NS)
            else -> {
                // Cross line via Old High Court
                val ihEW = eastWestStations.indexOfFirst { it.equals("Old High Court", ignoreCase = true) }
                val ihNS = northSouthStations.indexOfFirst { it.equals("Old High Court", ignoreCase = true) }

                val hopsToInterchange = if (idx1EW != -1) {
                    abs(idx1EW - if (ihEW != -1) ihEW else 7)
                } else if (idx1NS != -1) {
                    abs(idx1NS - if (ihNS != -1) ihNS else 8)
                } else 0

                val hopsFromInterchange = if (idx2EW != -1) {
                    abs(idx2EW - if (ihEW != -1) ihEW else 7)
                } else if (idx2NS != -1) {
                    abs(idx2NS - if (ihNS != -1) ihNS else 8)
                } else 0

                hopsToInterchange + hopsFromInterchange
            }
        }

        return FareCalculator.calculateTotalFare(hops, passengerCount, isReturnJourney = isReturn)
    }
}
