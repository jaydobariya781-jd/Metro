package com.example.m1

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

/**
 * Manages ticket storage, user preferences, and booking lifecycle using SharedPreferences
 */
object TicketManager {

    private const val PREF_NAME = "metro_ticket_prefs"
    private const val KEY_TICKETS = "saved_tickets_json"
    private const val KEY_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_PHONE = "user_phone"
    private const val KEY_USER_NAME = "user_name"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, value: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_LOGGED_IN, value).apply()
    }

    fun getUserPhone(context: Context): String {
        return getPrefs(context).getString(KEY_USER_PHONE, "+91 98765 43210") ?: "+91 98765 43210"
    }

    fun setUserPhone(context: Context, phone: String) {
        getPrefs(context).edit().putString(KEY_USER_PHONE, phone).apply()
    }

    fun getUserName(context: Context): String {
        return getPrefs(context).getString(KEY_USER_NAME, "Aarav Patel") ?: "Aarav Patel"
    }

    fun setUserName(context: Context, name: String) {
        getPrefs(context).edit().putString(KEY_USER_NAME, name).apply()
    }

    fun generateTicketId(context: Context): String {
        val count = getAllTickets(context).size + 1
        val timestamp = System.currentTimeMillis() % 1000000
        return "AMD-$timestamp-$count"
    }

    fun saveTicket(context: Context, ticket: TicketModel) {
        val tickets = getAllTickets(context).toMutableList()
        tickets.removeAll { it.ticketId == ticket.ticketId }
        tickets.add(0, ticket)
        persistTickets(context, tickets)
    }

    fun addTicket(context: Context, ticket: TicketModel) {
        saveTicket(context, ticket)
    }

    fun getAllTickets(context: Context): List<TicketModel> {
        val jsonString = getPrefs(context).getString(KEY_TICKETS, null) ?: return getSampleTickets()
        val list = mutableListOf<TicketModel>()
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    TicketModel(
                        ticketId = obj.getString("ticketId"),
                        fromStation = obj.getString("fromStation"),
                        toStation = obj.getString("toStation"),
                        passengers = obj.getInt("passengers"),
                        fare = obj.getDouble("fare"),
                        date = obj.getString("date"),
                        time = obj.getString("time"),
                        journeyType = obj.optString("journeyType", "Single Journey"),
                        paymentMethod = obj.optString("paymentMethod", "UPI"),
                        status = obj.optString("status", "ACTIVE"),
                        qrPayload = obj.optString("qrPayload", obj.getString("ticketId"))
                    )
                )
            }
        } catch (e: Exception) {
            return getSampleTickets()
        }
        return list
    }

    fun getTicketById(context: Context, ticketId: String): TicketModel? {
        return getAllTickets(context).find { it.ticketId == ticketId }
    }

    fun getActiveTickets(context: Context): List<TicketModel> {
        return getAllTickets(context).filter { it.status.equals("ACTIVE", ignoreCase = true) }
    }

    fun getPastTickets(context: Context): List<TicketModel> {
        return getAllTickets(context).filter { !it.status.equals("ACTIVE", ignoreCase = true) }
    }

    fun getHistoryTickets(context: Context): List<TicketModel> {
        return getPastTickets(context)
    }

    fun markTicketUsed(context: Context, ticketId: String) {
        val tickets = getAllTickets(context).toMutableList()
        val index = tickets.indexOfFirst { it.ticketId == ticketId }
        if (index != -1) {
            tickets[index].status = "USED"
            persistTickets(context, tickets)
        }
    }

    fun cancelTicket(context: Context, ticketId: String) {
        val tickets = getAllTickets(context).toMutableList()
        val index = tickets.indexOfFirst { it.ticketId == ticketId }
        if (index != -1) {
            tickets[index].status = "CANCELLED"
            persistTickets(context, tickets)
        }
    }

    private fun persistTickets(context: Context, tickets: List<TicketModel>) {
        val array = JSONArray()
        for (ticket in tickets) {
            val obj = JSONObject().apply {
                put("ticketId", ticket.ticketId)
                put("fromStation", ticket.fromStation)
                put("toStation", ticket.toStation)
                put("passengers", ticket.passengers)
                put("fare", ticket.fare)
                put("date", ticket.date)
                put("time", ticket.time)
                put("journeyType", ticket.journeyType)
                put("paymentMethod", ticket.paymentMethod)
                put("status", ticket.status)
                put("qrPayload", ticket.qrPayload)
            }
            array.put(obj)
        }
        getPrefs(context).edit().putString(KEY_TICKETS, array.toString()).apply()
    }

    private fun getSampleTickets(): List<TicketModel> {
        return listOf(
            TicketModel(
                ticketId = "AMD2026102301",
                fromStation = "Old High Court",
                toStation = "Vastral Gam",
                passengers = 1,
                fare = 25.0,
                date = "23 Oct 2026",
                time = "10:30 AM",
                journeyType = "Single Journey",
                status = "ACTIVE"
            ),
            TicketModel(
                ticketId = "AMD2026102202",
                fromStation = "Thaltej Gam",
                toStation = "Apparel Park",
                passengers = 2,
                fare = 40.0,
                date = "22 Oct 2026",
                time = "06:15 PM",
                journeyType = "Single Journey",
                status = "USED"
            )
        )
    }
}
