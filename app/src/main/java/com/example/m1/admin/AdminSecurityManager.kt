package com.example.m1.admin

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages admin authorization, elevated privileges, and session security
 */
class AdminSecurityManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("admin_sec_pref", Context.MODE_PRIVATE)

    companion object {
        private const val DEFAULT_PIN = "1234"
        private const val KEY_ADMIN_PIN = "admin_access_pin"
        private const val KEY_MAINTENANCE_MODE = "admin_maintenance_mode"

        @Volatile
        private var instance: AdminSecurityManager? = null

        fun getInstance(context: Context): AdminSecurityManager {
            return instance ?: synchronized(this) {
                instance ?: AdminSecurityManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun verifyPin(inputPin: String): Boolean {
        val storedPin = prefs.getString(KEY_ADMIN_PIN, DEFAULT_PIN) ?: DEFAULT_PIN
        return inputPin == storedPin
    }

    fun updatePin(oldPin: String, newPin: String): Boolean {
        if (!verifyPin(oldPin)) return false
        if (newPin.length < 4) return false
        prefs.edit().putString(KEY_ADMIN_PIN, newPin).apply()
        return true
    }

    fun isMaintenanceMode(): Boolean {
        return prefs.getBoolean(KEY_MAINTENANCE_MODE, false)
    }

    fun setMaintenanceMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_MAINTENANCE_MODE, enabled).apply()
    }
}
