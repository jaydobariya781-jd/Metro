package com.example.m1.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.m1.R

/**
 * Admin Panel Activity
 * Dedicated Admin Panel screen matching the requested System Overview design.
 */
class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        setupHeader()
        setupCards()
        setupTransactions()
        setupBottomNav()
    }

    private fun setupHeader() {
        // Notification bell click
        findViewById<ImageView>(R.id.iv_admin_bell)?.setOnClickListener {
            Toast.makeText(this, "Ahmedabad Metro: All systems operational", Toast.LENGTH_SHORT).show()
        }

        // Admin profile avatar click
        findViewById<ImageView>(R.id.iv_admin_profile)?.setOnClickListener {
            Toast.makeText(this, "Logged in as Metro Administrator", Toast.LENGTH_SHORT).show()
        }

        // Tap Admin logo/title to exit back to passenger app
        findViewById<TextView>(R.id.tv_admin_brand)?.setOnClickListener {
            finish()
        }
    }

    private fun setupCards() {
        findViewById<CardView>(R.id.card_total_users)?.setOnClickListener {
            startActivity(Intent(this, AdminUsersActivity::class.java))
        }

        findViewById<CardView>(R.id.card_daily_tickets)?.setOnClickListener {
            startActivity(Intent(this, AdminDailyTicketsActivity::class.java))
        }

        findViewById<CardView>(R.id.card_revenue)?.setOnClickListener {
            startActivity(Intent(this, AdminRevenueActivity::class.java))
        }
    }

    private fun setupTransactions() {
        findViewById<TextView>(R.id.tv_admin_view_all)?.setOnClickListener {
            startActivity(Intent(this, AdminRevenueActivity::class.java))
        }

        findViewById<CardView>(R.id.card_trans_1)?.setOnClickListener {
            Toast.makeText(this, "Transaction #AMD20260901: Aarav Patel • ₹25.00 (COMPLETED)", Toast.LENGTH_SHORT).show()
        }

        findViewById<CardView>(R.id.card_trans_2)?.setOnClickListener {
            Toast.makeText(this, "Transaction #AMD20260902: Kabir Mehta • ₹15.00 (PENDING)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNav() {
        findViewById<LinearLayout>(R.id.nav_admin_home)?.setOnClickListener {
            // Already on home dashboard
        }

        findViewById<LinearLayout>(R.id.nav_admin_users)?.setOnClickListener {
            startActivity(Intent(this, AdminUsersActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_admin_settings)?.setOnClickListener {
            startActivity(Intent(this, AdminSettingsActivity::class.java))
        }
    }
}
