package com.example.m1.admin

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.m1.LoginActivity
import com.example.m1.R
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Admin Settings Activity
 * Manages Admin Profile, SMS Notifications, Dark Mode, Logout, and Navigation.
 */
class AdminSettingsActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etMobile: EditText
    private lateinit var btnSaveChanges: MaterialButton
    private lateinit var switchSms: SwitchCompat
    private lateinit var switchDarkMode: SwitchCompat
    private lateinit var ivAvatar: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_settings)

        initViews()
        setupHeader()
        setupProfileForm()
        setupToggles()
        setupBottomNav()
    }

    private fun initViews() {
        etFullName = findViewById(R.id.et_admin_full_name)
        etMobile = findViewById(R.id.et_admin_mobile)
        btnSaveChanges = findViewById(R.id.btn_save_changes)
        switchSms = findViewById(R.id.switch_sms_notifications)
        switchDarkMode = findViewById(R.id.switch_dark_mode)
        ivAvatar = findViewById(R.id.iv_settings_avatar)
    }

    private fun setupHeader() {
        // Brand click takes back to Admin Home
        findViewById<TextView>(R.id.tv_admin_brand)?.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }

        // Logout Button Click
        findViewById<TextView>(R.id.btn_admin_logout)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out of the Metro Admin Console?")
                .setPositiveButton("Logout") { _, _ ->
                    Toast.makeText(this, "Logged out from Admin Console", Toast.LENGTH_SHORT).show()
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

    private fun setupProfileForm() {
        // Avatar Click
        ivAvatar.setOnClickListener {
            val options = arrayOf("Take New Photo", "Choose from Gallery", "Reset to Default Avatar")
            AlertDialog.Builder(this)
                .setTitle("Change Admin Avatar")
                .setItems(options) { _, which ->
                    Toast.makeText(this, "Avatar updated: ${options[which]}", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Save Changes Button Click
        btnSaveChanges.setOnClickListener {
            val name = etFullName.text.toString().trim()
            val mobile = etMobile.text.toString().trim()

            if (name.isEmpty()) {
                etFullName.error = "Name cannot be empty"
                return@setOnClickListener
            }
            if (mobile.isEmpty()) {
                etMobile.error = "Mobile cannot be empty"
                return@setOnClickListener
            }

            Toast.makeText(this, "Admin profile saved: $name ($mobile)", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupToggles() {
        // SMS Notifications Toggle Click
        switchSms.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Enabled" else "Disabled"
            Toast.makeText(this, "SMS Notifications: $status", Toast.LENGTH_SHORT).show()
        }

        // Dark Mode Toggle Click
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Enabled" else "Disabled"
            Toast.makeText(this, "Dark Mode: $status", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNav() {
        // Tab 1: Home
        findViewById<LinearLayout>(R.id.nav_admin_home)?.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }

        // Tab 2: Users
        findViewById<LinearLayout>(R.id.nav_admin_users)?.setOnClickListener {
            val intent = Intent(this, AdminUsersActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }

        // Tab 3: Settings (already here)
        findViewById<LinearLayout>(R.id.nav_admin_settings)?.setOnClickListener {
            // Already here
        }
    }
}
