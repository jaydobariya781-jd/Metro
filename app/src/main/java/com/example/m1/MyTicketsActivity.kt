package com.example.m1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * MyTicketsActivity — Brij Nagvadiya
 * Standalone My Tickets screen (used from Settings bottom nav).
 * Displays active and history tabs, mirrors TicketsFragment behavior.
 */
class MyTicketsActivity : AppCompatActivity() {

    private lateinit var llTicketsContainer: LinearLayout
    private lateinit var tabActive: TextView
    private lateinit var tabHistory: TextView
    private var isHistoryTab = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_tickets)

        isHistoryTab = intent.getBooleanExtra("SHOW_HISTORY", false)

        llTicketsContainer = findViewById(R.id.ll_tickets_container)
        tabActive = findViewById(R.id.tab_active)
        tabHistory = findViewById(R.id.tab_history)

        setupTabs()
        setupBookButton()
        setupBottomNav()
        loadTickets()
    }

    override fun onResume() {
        super.onResume()
        loadTickets()
    }

    private fun setupTabs() {
        tabActive.setOnClickListener {
            isHistoryTab = false
            updateTabUI()
            loadTickets()
        }
        tabHistory.setOnClickListener {
            isHistoryTab = true
            updateTabUI()
            loadTickets()
        }
        updateTabUI()
    }

    private fun updateTabUI() {
        if (!isHistoryTab) {
            tabActive.setBackgroundResource(R.drawable.bg_tab_selected)
            tabActive.setTextColor(Color.parseColor("#1A56C4"))
            tabHistory.setBackgroundColor(Color.TRANSPARENT)
            tabHistory.setTextColor(Color.parseColor("#888888"))
        } else {
            tabHistory.setBackgroundResource(R.drawable.bg_tab_selected)
            tabHistory.setTextColor(Color.parseColor("#1A56C4"))
            tabActive.setBackgroundColor(Color.TRANSPARENT)
            tabActive.setTextColor(Color.parseColor("#888888"))
        }
    }

    private fun setupBookButton() {
        findViewById<Button>(R.id.btn_book_new_ticket)?.setOnClickListener {
            startActivity(Intent(this, BookTicketActivity::class.java))
        }
    }

    private fun loadTickets() {
        llTicketsContainer.removeAllViews()
        val tickets = if (isHistoryTab) {
            TicketManager.getHistoryTickets(this)
        } else {
            TicketManager.getActiveTickets(this)
        }

        if (tickets.isEmpty()) {
            val emptyTv = TextView(this).apply {
                text = if (isHistoryTab)
                    "No past trips found.\nCompleted metro trips will appear here."
                else
                    "No active tickets.\nTap '+ Book' to book a metro ticket!"
                textSize = 14f
                setTextColor(Color.parseColor("#888888"))
                textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
                setPadding(20, 60, 20, 40)
            }
            llTicketsContainer.addView(emptyTv)
        } else {
            val inflater = LayoutInflater.from(this)
            for (ticket in tickets) {
                val card = inflater.inflate(R.layout.item_ticket_card, llTicketsContainer, false)
                card.findViewById<TextView>(R.id.tv_ticket_route)?.text =
                    "${ticket.fromStation} → ${ticket.toStation}"
                val passLabel = if (ticket.passengers == 1) "1 Pass." else "${ticket.passengers} Pass."
                card.findViewById<TextView>(R.id.tv_ticket_datetime)?.text =
                    "${ticket.date} • ${ticket.time} • $passLabel"
                card.findViewById<TextView>(R.id.tv_ticket_id)?.text = "ID: ${ticket.ticketId}"
                card.findViewById<TextView>(R.id.tv_ticket_fare)?.text =
                    "₹${"%.2f".format(ticket.fare)}"
                card.findViewById<TextView>(R.id.tv_ticket_status)?.text = ticket.status
                card.setOnClickListener {
                    startActivity(Intent(this, BookingSuccessActivity::class.java).apply {
                        putExtra("TICKET_ID", ticket.ticketId)
                    })
                }
                llTicketsContainer.addView(card)
            }
        }
    }

    private fun setupBottomNav() {
        findViewById<LinearLayout>(R.id.nav_user_home)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_user_tickets)?.setOnClickListener { /* Already here */ }
        findViewById<LinearLayout>(R.id.nav_user_map)?.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java)); finish()
        }
        findViewById<LinearLayout>(R.id.nav_user_profile)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java)); finish()
        }
    }
}
