package com.example.m1

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

/**
 * User Settings Activity
 * Matches the passenger app settings screen with interactive Account management,
 * Notification toggles, Privacy & Location services, General preferences (Language & Theme),
 * Sign Out flow, and bottom navigation.
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var tvProfileTitle: TextView
    private lateinit var tvProfileSub: TextView
    private lateinit var switchPush: SwitchCompat
    private lateinit var switchSms: SwitchCompat
    private lateinit var tvLocationMode: TextView
    private lateinit var tvSelectedLanguage: TextView

    // Theme toggle components
    private lateinit var layoutThemeToggle: LinearLayout
    private lateinit var flThemeSun: FrameLayout
    private lateinit var flThemeMoon: FrameLayout
    private lateinit var ivThemeSun: ImageView
    private lateinit var ivThemeMoon: ImageView

    private var isDarkMode = false
    private var userName = "Rahul Sharma"
    private var userPhone = "+91 98765 43210"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadPreferences()
        initViews()
        setupHeader()
        setupAccountSection()
        setupNotificationSection()
        setupPrivacySection()
        setupGeneralSection()
        setupSignOut()
        setupBottomNav()
    }

    private fun loadPreferences() {
        val prefs = getSharedPreferences("user_settings_prefs", Context.MODE_PRIVATE)
        userName = prefs.getString("user_name", "Rahul Sharma") ?: "Rahul Sharma"
        userPhone = TicketManager.getUserPhone(this).ifEmpty { "+91 98765 43210" }
        isDarkMode = prefs.getBoolean("dark_theme", false)
    }

    private fun initViews() {
        tvProfileTitle = findViewById(R.id.tv_settings_profile_name)
        tvProfileSub = findViewById(R.id.tv_settings_profile_sub)
        switchPush = findViewById(R.id.switch_push_notifications)
        switchSms = findViewById(R.id.switch_sms_alerts)
        tvLocationMode = findViewById(R.id.tv_location_mode)
        tvSelectedLanguage = findViewById(R.id.tv_selected_language)

        layoutThemeToggle = findViewById(R.id.layout_theme_toggle)
        flThemeSun = findViewById(R.id.fl_theme_sun)
        flThemeMoon = findViewById(R.id.fl_theme_moon)
        ivThemeSun = findViewById(R.id.iv_theme_sun)
        ivThemeMoon = findViewById(R.id.iv_theme_moon)

        updateThemeUI(isDarkMode)
    }

    private fun setupHeader() {
        // Back Button
        findViewById<ImageView>(R.id.btn_settings_back)?.setOnClickListener {
            finish()
        }

        // Title Click
        findViewById<TextView>(R.id.tv_settings_title)?.setOnClickListener {
            Toast.makeText(this, "Ahmedabad Metro • Passenger Settings", Toast.LENGTH_SHORT).show()
        }

        // Help Button (?)
        findViewById<ImageView>(R.id.btn_settings_help)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Settings Help & FAQ")
                .setMessage(
                    "• Profile Information: Update your name, phone number, and metro card link.\n\n" +
                    "• Push Notifications: Instant train schedule updates, line delays, and gate status.\n\n" +
                    "• SMS Alerts: Receive weekly journey receipts and summary statements.\n\n" +
                    "• Location Services: Detects nearest Ahmedabad Metro station automatically.\n\n" +
                    "• 24x7 Customer Support: 1800-419-4454"
                )
                .setPositiveButton("Helpline") { _, _ ->
                    Toast.makeText(this, "Calling Metro Helpline: 1800-419-4454", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Close", null)
                .show()
        }
    }

    private fun setupAccountSection() {
        findViewById<CardView>(R.id.card_profile_info)?.setOnClickListener {
            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 30, 50, 10)
            }

            val etName = EditText(this).apply {
                hint = "Full Name"
                setText(userName)
            }

            val etPhone = EditText(this).apply {
                hint = "Phone Number"
                setText(userPhone)
            }

            layout.addView(etName)
            layout.addView(etPhone)

            AlertDialog.Builder(this)
                .setTitle("Edit Profile Information")
                .setView(layout)
                .setPositiveButton("Save") { _, _ ->
                    val newName = etName.text.toString().trim()
                    val newPhone = etPhone.text.toString().trim()

                    if (newName.isNotEmpty()) {
                        userName = newName
                        userPhone = newPhone
                        getSharedPreferences("user_settings_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("user_name", userName)
                            .putString("user_phone", userPhone)
                            .apply()

                        tvProfileSub.text = "$userName • $userPhone"
                        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupNotificationSection() {
        // Push Switch
        switchPush.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) {
                "Push Notifications enabled (Real-time delay alerts ON)"
            } else {
                "Push Notifications disabled"
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Tap entire push row to toggle
        findViewById<RelativeLayout>(R.id.row_push_notifications)?.setOnClickListener {
            switchPush.isChecked = !switchPush.isChecked
        }

        // SMS Switch
        switchSms.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) {
                "SMS Alerts enabled (Weekly summaries sent to $userPhone)"
            } else {
                "SMS Alerts disabled"
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Tap entire SMS row to toggle
        findViewById<RelativeLayout>(R.id.row_sms_alerts)?.setOnClickListener {
            switchSms.isChecked = !switchSms.isChecked
        }
    }

    private fun setupPrivacySection() {
        val locationOptions = arrayOf("Always", "While Using the App", "Never")

        findViewById<CardView>(R.id.card_location_services)?.setOnClickListener {
            val currentSelected = locationOptions.indexOf(tvLocationMode.text.toString()).let {
                if (it >= 0) it else 0
            }

            AlertDialog.Builder(this)
                .setTitle("Location Permission")
                .setSingleChoiceItems(locationOptions, currentSelected) { dialog, which ->
                    val chosen = locationOptions[which]
                    tvLocationMode.text = chosen
                    Toast.makeText(this, "Location access set to '$chosen'", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupGeneralSection() {
        // Language Selection
        val languageOptions = arrayOf(
            "English (US)",
            "English (IN)",
            "Gujarati (ગુજરાતી)",
            "Hindi (हिन्दी)"
        )

        findViewById<RelativeLayout>(R.id.row_language)?.setOnClickListener {
            val currentSelected = languageOptions.indexOf(tvSelectedLanguage.text.toString()).let {
                if (it >= 0) it else 0
            }

            AlertDialog.Builder(this)
                .setTitle("Select App Language")
                .setSingleChoiceItems(languageOptions, currentSelected) { dialog, which ->
                    val chosenLang = languageOptions[which]
                    tvSelectedLanguage.text = chosenLang
                    Toast.makeText(this, "Language changed to $chosenLang", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Theme Toggle Pill Click
        layoutThemeToggle.setOnClickListener {
            toggleTheme()
        }

        findViewById<RelativeLayout>(R.id.row_theme)?.setOnClickListener {
            toggleTheme()
        }
    }

    private fun toggleTheme() {
        isDarkMode = !isDarkMode
        getSharedPreferences("user_settings_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("dark_theme", isDarkMode)
            .apply()

        updateThemeUI(isDarkMode)

        val modeName = if (isDarkMode) "Dark Mode" else "Light Mode"
        Toast.makeText(this, "Theme switched to $modeName", Toast.LENGTH_SHORT).show()
    }

    private fun updateThemeUI(dark: Boolean) {
        if (dark) {
            // Moon active
            flThemeMoon.background = ContextCompat.getDrawable(this, R.drawable.bg_theme_moon_active)
            ivThemeMoon.setColorFilter(Color.WHITE)

            // Sun inactive
            flThemeSun.background = null
            ivThemeSun.setColorFilter(Color.parseColor("#94A3B8"))
        } else {
            // Sun active
            flThemeSun.background = ContextCompat.getDrawable(this, R.drawable.bg_theme_sun_active)
            ivThemeSun.setColorFilter(Color.parseColor("#F59E0B"))

            // Moon inactive
            flThemeMoon.background = null
            ivThemeMoon.setColorFilter(Color.parseColor("#94A3B8"))
        }
    }

    private fun setupSignOut() {
        findViewById<LinearLayout>(R.id.btn_sign_out)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out from Ahmedabad Metro App?")
                .setPositiveButton("Sign Out") { _, _ ->
                    TicketManager.logout(this)
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupBottomNav() {
        // Tab 1: Home
        findViewById<LinearLayout>(R.id.nav_user_home)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }

        // Tab 2: Tickets
        findViewById<LinearLayout>(R.id.nav_user_tickets)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
            finish()
        }

        // Tab 3: Map
        findViewById<LinearLayout>(R.id.nav_user_map)?.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
            finish()
        }

        // Tab 4: Profile
        findViewById<LinearLayout>(R.id.nav_user_profile)?.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }
    }
}
