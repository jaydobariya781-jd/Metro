package com.example.m1.admin

import java.io.Serializable

/**
 * Data model for station management in admin dashboard
 */
data class AdminStationModel(
    val id: String,
    val name: String,
    val lineName: String,
    val lineColor: String,
    val platforms: Int = 2,
    val isInterchange: Boolean = false,
    val hasElevator: Boolean = true,
    val hasParking: Boolean = true,
    var isOperational: Boolean = true,
    val firstTrainTime: String = "06:00 AM",
    val lastTrainTime: String = "10:30 PM"
) : Serializable
