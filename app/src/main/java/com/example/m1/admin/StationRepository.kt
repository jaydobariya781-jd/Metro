package com.example.m1.admin

import android.content.Context
import android.content.SharedPreferences

/**
 * Repository to query, filter, update and manage metro stations
 */
class StationRepository private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("admin_stations_pref", Context.MODE_PRIVATE)

    private val defaultStations = mutableListOf(
        AdminStationModel("ST01", "Vastral Gam", "East-West (Blue)", "#2563EB", 2, false),
        AdminStationModel("ST02", "Nirant Cross Road", "East-West (Blue)", "#2563EB", 2, false),
        AdminStationModel("ST03", "Vastral", "East-West (Blue)", "#2563EB", 2, false),
        AdminStationModel("ST04", "Rabari Colony", "East-West (Blue)", "#2563EB", 2, false),
        AdminStationModel("ST05", "Amraiwadi", "East-West (Blue)", "#2563EB", 2, false),
        AdminStationModel("ST06", "Apparel Park", "East-West (Blue)", "#2563EB", 4, false),
        AdminStationModel("ST07", "Kalupur Railway Station", "East-West (Blue)", "#2563EB", 2, true),
        AdminStationModel("ST08", "Old High Court", "Interchange", "#10B981", 4, true),
        AdminStationModel("ST09", "Thaltej Gam", "East-West (Blue)", "#2563EB", 2, false),
        AdminStationModel("ST10", "APMC", "North-South (Red)", "#EF4444", 2, false),
        AdminStationModel("ST11", "Jivraj Park", "North-South (Red)", "#EF4444", 2, false),
        AdminStationModel("ST12", "Paldi", "North-South (Red)", "#EF4444", 2, false),
        AdminStationModel("ST13", "Gandhinagar Sector 1", "North-South (Red)", "#EF4444", 2, false),
        AdminStationModel("ST14", "Motera Stadium", "North-South (Red)", "#EF4444", 4, true)
    )

    fun getAllStations(): List<AdminStationModel> {
        return defaultStations.toList()
    }

    fun getStationById(id: String): AdminStationModel? {
        return defaultStations.find { it.id.equals(id, ignoreCase = true) }
    }

    fun getStationsByLine(line: String): List<AdminStationModel> {
        return defaultStations.filter { it.lineName.contains(line, ignoreCase = true) }
    }

    fun toggleStationStatus(stationId: String): Boolean {
        val station = defaultStations.find { it.id == stationId }
        return if (station != null) {
            station.isOperational = !station.isOperational
            prefs.edit().putBoolean("status_${station.id}", station.isOperational).apply()
            station.isOperational
        } else {
            false
        }
    }

    fun getInterchangeStations(): List<AdminStationModel> {
        return defaultStations.filter { it.isInterchange }
    }

    fun searchStations(query: String): List<AdminStationModel> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return getAllStations()
        return defaultStations.filter {
            it.name.contains(trimmed, ignoreCase = true) ||
                    it.lineName.contains(trimmed, ignoreCase = true) ||
                    it.id.contains(trimmed, ignoreCase = true)
        }
    }

    companion object {
        @Volatile
        private var instance: StationRepository? = null

        fun getInstance(context: Context): StationRepository {
            return instance ?: synchronized(this) {
                instance ?: StationRepository(context.applicationContext).also { instance = it }
            }
        }
    }
}
