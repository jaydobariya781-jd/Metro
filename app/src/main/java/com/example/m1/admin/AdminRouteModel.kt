package com.example.m1.admin

import java.io.Serializable

/**
 * Data class representing an operational metro route or corridor
 */
data class AdminRouteModel(
    val routeCode: String,
    val sourceStation: String,
    val destinationStation: String,
    val totalStations: Int,
    val approximateTimeMinutes: Int,
    val standardFare: Int,
    val corridorType: String,
    val peakFrequencyMinutes: Int = 5,
    val offPeakFrequencyMinutes: Int = 10,
    var isActive: Boolean = true
) : Serializable
