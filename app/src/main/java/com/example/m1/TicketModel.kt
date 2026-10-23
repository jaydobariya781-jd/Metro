package com.example.m1

import java.io.Serializable

/**
 * Data class representing a metro passenger ticket or daily pass
 */
data class TicketModel(
    val ticketId: String,
    val fromStation: String,
    val toStation: String,
    val passengers: Int,
    val fare: Double,
    val date: String,
    val time: String,
    val journeyType: String = "Single Journey",
    val paymentMethod: String = "UPI",
    var status: String = "ACTIVE", // ACTIVE, USED, EXPIRED, CANCELLED
    val qrPayload: String = ticketId
) : Serializable
