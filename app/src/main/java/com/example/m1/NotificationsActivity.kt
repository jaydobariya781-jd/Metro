package com.example.m1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

/**
 * Notifications Activity
 * Displays live metro service alerts, ticket confirmations, daily pass promos,
 * station maintenance updates, with interactive filtering and direct action buttons.
 */
class NotificationsActivity : AppCompatActivity() {

    private lateinit var cardNotif1: CardView
    private lateinit var cardNotif2: CardView
    private lateinit var cardNotif3: CardView
    private lateinit var cardNotif4: CardView
    private lateinit var llContainer: LinearLayout

    private lateinit var chipAll: TextView
    private lateinit var chipAlerts: TextView
    private lateinit var chipOffers: TextView
    private lateinit var chipTickets: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        initViews()
        setupHeader()
        setupFilters()
        setupNotificationActions()
        setupBottomNav()
    }

    private fun initViews() {
        cardNotif1 = findViewById(R.id.card_notif_1)
        cardNotif2 = findViewById(R.id.card_notif_2)
        cardNotif3 = findViewById(R.id.card_notif_3)
        cardNotif4 = findViewById(R.id.card_notif_4)
        llContainer = findViewById(R.id.ll_notifications_container)

        chipAll = findViewById(R.id.chip_filter_all)
        chipAlerts = findViewById(R.id.chip_filter_alerts)
        chipOffers = findViewById(R.id.chip_filter_offers)
        chipTickets = findViewById(R.id.chip_filter_tickets)
    }

    private fun setupHeader() {
        // Back Button
        findViewById<ImageView>(R.id.btn_notifications_back)?.setOnClickListener {
            finish()
        }

        // Title click
        findViewById<TextView>(R.id.tv_notifications_title)?.setOnClickListener {
            Toast.makeText(this, "Ahmedabad Metro • Notifications Center", Toast.LENGTH_SHORT).show()
        }

        // Mark all read
        findViewById<TextView>(R.id.tv_mark_all_read)?.setOnClickListener {
            Toast.makeText(this, "All 4 notifications marked as read ✓", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupFilters() {
        val chips = listOf(chipAll, chipAlerts, chipOffers, chipTickets)

        fun selectChip(selected: TextView) {
            chips.forEach { chip ->
                if (chip == selected) {
                    chip.background = ContextCompat.getDrawable(this, R.drawable.bg_filter_pill_active)
                    chip.setTextColor(Color.WHITE)
                } else {
                    chip.background = ContextCompat.getDrawable(this, R.drawable.bg_filter_pill_inactive)
                    chip.setTextColor(Color.parseColor("#64748B"))
                }
            }
        }

        chipAll.setOnClickListener {
            selectChip(chipAll)
            cardNotif1.visibility = View.VISIBLE
            cardNotif2.visibility = View.VISIBLE
            cardNotif3.visibility = View.VISIBLE
            cardNotif4.visibility = View.VISIBLE
        }

        chipAlerts.setOnClickListener {
            selectChip(chipAlerts)
            cardNotif1.visibility = View.VISIBLE
            cardNotif2.visibility = View.GONE
            cardNotif3.visibility = View.GONE
            cardNotif4.visibility = View.VISIBLE
        }

        chipOffers.setOnClickListener {
            selectChip(chipOffers)
            cardNotif1.visibility = View.GONE
            cardNotif2.visibility = View.VISIBLE
            cardNotif3.visibility = View.GONE
            cardNotif4.visibility = View.GONE
        }

        chipTickets.setOnClickListener {
            selectChip(chipTickets)
            cardNotif1.visibility = View.GONE
            cardNotif2.visibility = View.GONE
            cardNotif3.visibility = View.VISIBLE
            cardNotif4.visibility = View.GONE
        }
    }

    private fun setupNotificationActions() {
        // Card 1: Service Alert
        cardNotif1.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Metro Line 1 Status")
                .setMessage("All trains between Thaltej and Vastral Gam are operating normally.\n\n• Headway: Every 5 minutes\n• Average Speed: 34 km/h\n• Peak Hours: 08:30 AM - 11:00 AM\n• Safety: All automatic signaling green")
                .setPositiveButton("View Metro Map") { _, _ ->
                    startActivity(Intent(this, MapActivity::class.java))
                }
                .setNegativeButton("Close", null)
                .show()
        }

        // Card 2 Action: Get Daily Pass
        findViewById<TextView>(R.id.btn_notif_get_pass)?.setOnClickListener {
            startActivity(Intent(this, DailyPassOtpActivity::class.java))
        }

        cardNotif2.setOnClickListener {
            startActivity(Intent(this, DailyPassOtpActivity::class.java))
        }

        // Card 3 Action: View My Ticket
        findViewById<TextView>(R.id.btn_notif_view_ticket)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
        }

        cardNotif3.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
        }

        // Card 4: Facility Update
        cardNotif4.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Station Facility Update")
                .setMessage("Old High Court Interchange Station:\n\n• Gate 1: Open & Operational\n• Gate 2 (Elevator & Escalator): Reopened after safety inspection\n• Gate 3: Open\n• Ticket Counters: 4 Active")
                .setPositiveButton("OK", null)
                .show()
        }

        // Clear All
        findViewById<TextView>(R.id.btn_notifications_clear_all)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear All Notifications")
                .setMessage("Do you want to clear all your current notifications?")
                .setPositiveButton("Clear All") { _, _ ->
                    llContainer.removeAllViews()
                    val emptyTv = TextView(this).apply {
                        text = "No new notifications 🎉"
                        textSize = 15f
                        setTextColor(Color.parseColor("#94A3B8"))
                        setPadding(0, 80, 0, 80)
                        gravity = android.view.Gravity.CENTER
                    }
                    llContainer.addView(emptyTv)
                    Toast.makeText(this, "Notifications cleared", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupBottomNav() {
        // Tab 1: Home
        findViewById<LinearLayout>(R.id.nav_notif_home)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }

        // Tab 2: Tickets
        findViewById<LinearLayout>(R.id.nav_notif_tickets)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
            finish()
        }

        // Tab 3: Map
        findViewById<LinearLayout>(R.id.nav_notif_map)?.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
            finish()
        }

        // Tab 4: Profile
        findViewById<LinearLayout>(R.id.nav_notif_profile)?.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }
    }
}
