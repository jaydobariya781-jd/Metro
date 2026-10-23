package com.example.m1.admin

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * System audit logger for tracking admin actions and security occurrences
 */
class AdminAuditLog private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("admin_audit_pref", Context.MODE_PRIVATE)

    data class LogEntry(
        val timestamp: String,
        val adminUser: String,
        val action: String,
        val details: String
    )

    fun logAction(action: String, details: String) {
        val now = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault()).format(Date())
        val entry = "$now|$action|$details"
        val existing = prefs.getString("audit_logs", "") ?: ""
        val updated = if (existing.isEmpty()) entry else "$entry\n$existing"
        prefs.edit().putString("audit_logs", updated).apply()
    }

    fun getLogs(): List<LogEntry> {
        val raw = prefs.getString("audit_logs", "") ?: ""
        if (raw.isEmpty()) return getInitialLogs()

        return raw.lines().filter { it.contains("|") }.map { line ->
            val parts = line.split("|")
            LogEntry(
                timestamp = parts.getOrElse(0) { "" },
                adminUser = "Admin",
                action = parts.getOrElse(1) { "Action" },
                details = parts.getOrElse(2) { "" }
            )
        }
    }

    private fun getInitialLogs(): List<LogEntry> {
        return listOf(
            LogEntry("23 Oct 2026, 16:15:00", "Admin", "STATION_UPDATE", "Updated platform status for Old High Court"),
            LogEntry("23 Oct 2026, 15:40:22", "Admin", "USER_STATUS_CHANGE", "Verified commuter ID #AMD20260901"),
            LogEntry("23 Oct 2026, 14:10:05", "Admin", "FARE_CONFIG", "Reviewed off-peak concession guidelines"),
            LogEntry("23 Oct 2026, 11:00:18", "Admin", "SYSTEM_CHECK", "Automated turnstile gateway diagnostics passed")
        )
    }

    companion object {
        @Volatile
        private var instance: AdminAuditLog? = null

        fun getInstance(context: Context): AdminAuditLog {
            return instance ?: synchronized(this) {
                instance ?: AdminAuditLog(context.applicationContext).also { instance = it }
            }
        }
    }
}
