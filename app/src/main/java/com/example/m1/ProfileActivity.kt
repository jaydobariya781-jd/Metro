package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.m1.admin.AdminActivity

/**
 * ProfileActivity — Brij Nagvadiya
 * Standalone profile screen accessible from Settings bottom nav.
 * Mirrors ProfileFragment with full navigation capabilities.
 */
class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupHeader()
        loadUserInfo()
        setupMenuActions()
        setupBottomNav()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_profile_back)?.setOnClickListener { finish() }
    }

    private fun loadUserInfo() {
        val userPhone = TicketManager.getUserPhone(this)
        val userName = TicketManager.getUserName(this)
        findViewById<TextView>(R.id.tv_profile_name)?.text = userName
        findViewById<TextView>(R.id.tv_profile_phone)?.text = userPhone
    }

    private fun setupMenuActions() {
        findViewById<ImageView>(R.id.btn_edit_profile)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.row_my_tickets)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.row_trip_history)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java).apply {
                putExtra("SHOW_HISTORY", true)
            })
        }
        findViewById<LinearLayout>(R.id.row_saved_stations)?.setOnClickListener {
            startActivity(Intent(this, SavedStationsActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.row_payment_methods)?.setOnClickListener {
            startActivity(Intent(this, DailyPassOtpActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.row_settings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.row_help)?.setOnClickListener {
            startActivity(Intent(this, HelpSupportActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.row_admin_panel)?.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
        findViewById<Button>(R.id.btn_logout)?.setOnClickListener {
            TicketManager.logout(this)
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

    private fun setupBottomNav() {
        findViewById<LinearLayout>(R.id.nav_user_home)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_user_tickets)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_user_map)?.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_user_profile)?.setOnClickListener { /* Already here */ }
    }
}
