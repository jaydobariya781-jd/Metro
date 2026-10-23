package com.example.m1.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.m1.R

/**
 * Admin Daily Tickets Activity
 * Displays comprehensive daily ticketing statistics, breakdown statuses (Completed, Pending, Cancelled),
 * and recent passenger tickets with interactive verification, live validation, and detailed breakdowns.
 */
class AdminDailyTicketsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_daily_tickets)

        setupHeader()
        setupHeroCard()
        setupStatusCards()
        setupRecentTickets()
        setupBottomNav()
    }

    private fun setupHeader() {
        // Back Button
        findViewById<ImageView>(R.id.btn_tickets_back)?.setOnClickListener {
            finish()
        }

        // Title click
        findViewById<TextView>(R.id.tv_tickets_title)?.setOnClickListener {
            Toast.makeText(this, "Daily Tickets Dashboard - Ahmedabad Metro", Toast.LENGTH_SHORT).show()
        }

        // Notification Bell
        findViewById<ImageView>(R.id.iv_tickets_bell)?.setOnClickListener {
            Toast.makeText(this, "Ticketing Alert: Morning rush peaked at 09:30 AM (320 tickets/hr)", Toast.LENGTH_SHORT).show()
        }

        // Profile Avatar
        findViewById<ImageView>(R.id.iv_tickets_profile)?.setOnClickListener {
            Toast.makeText(this, "Logged in as Metro Administrator", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupHeroCard() {
        // Hero Card click: Summary Dialog
        findViewById<CardView>(R.id.card_hero_tickets)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Today's Tickets Summary")
                .setMessage(
                    "• Total Tickets Sold: 1,240 (+8.2% vs yesterday)\n" +
                    "• Peak Demand Hour: 09:30 AM (320 tickets)\n" +
                    "• Total Completed Trips: 1,152 (92.9%)\n" +
                    "• Active / In-Transit: 68 (5.5%)\n" +
                    "• Cancelled / Refunded: 20 (1.6%)\n" +
                    "• Total Revenue Generated: ₹45,210.00"
                )
                .setPositiveButton("OK", null)
                .show()
        }

        // Growth Badge click
        findViewById<TextView>(R.id.tv_tickets_growth_badge)?.setOnClickListener {
            Toast.makeText(this, "+8.2% increase in passenger ticket sales compared to yesterday", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupStatusCards() {
        // Card 1: Completed Tickets
        findViewById<LinearLayout>(R.id.card_completed_tickets)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Completed Tickets (1,152)")
                .setMessage(
                    "• Successfully scanned at exit turnstiles: 1,152\n" +
                    "• Single Journey Tickets: 984\n" +
                    "• Return Journey Tickets: 168\n" +
                    "• Average Travel Duration: 24 mins\n" +
                    "• Top Route: Thaltej ↔ Vastral Gam"
                )
                .setPositiveButton("Export Report") { _, _ ->
                    Toast.makeText(this, "Completed tickets report exported (PDF/CSV)", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Close", null)
                .show()
        }

        // Card 2: Pending Tickets
        findViewById<LinearLayout>(R.id.card_pending_tickets)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Pending Tickets (68)")
                .setMessage(
                    "• Currently active and valid for today\n" +
                    "• Waiting at entrance turnstiles: 42\n" +
                    "• Currently in transit on train: 26\n" +
                    "• Valid until Metro closing (11:00 PM)"
                )
                .setPositiveButton("Refresh Live Feed") { _, _ ->
                    Toast.makeText(this, "Turnstile live passenger data refreshed", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Close", null)
                .show()
        }

        // Card 3: Cancelled Tickets
        findViewById<LinearLayout>(R.id.card_cancelled_tickets)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cancelled Tickets (20)")
                .setMessage(
                    "• Passenger self-cancellation: 14\n" +
                    "• Gate entry timeout (>30 min): 4\n" +
                    "• Payment gateway reversals: 2\n" +
                    "• Total Refund Processed: ₹580.00"
                )
                .setPositiveButton("View Refund Log") { _, _ ->
                    Toast.makeText(this, "Opening automated refund transactions log", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Close", null)
                .show()
        }
    }

    private fun setupRecentTickets() {
        // View All click
        findViewById<TextView>(R.id.tv_tickets_view_all)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("All Daily Tickets (1,240)")
                .setMessage(
                    "Corridor Distribution for Today:\n\n" +
                    "• Thaltej ↔ Vastral Gam: 520 tickets (41.9%)\n" +
                    "• Sabarmati ↔ Old High Court: 380 tickets (30.6%)\n" +
                    "• Motera Stadium ↔ APMC: 220 tickets (17.7%)\n" +
                    "• Other Corridors: 120 tickets (9.8%)\n\n" +
                    "Peak Turnstile Station: Old High Court (Interchange)"
                )
                .setPositiveButton("Download CSV") { _, _ ->
                    Toast.makeText(this, "Downloading Daily_Tickets_20260929.csv...", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Close", null)
                .show()
        }

        // Ticket 1: Aarav Patel
        findViewById<CardView>(R.id.card_ticket_1)?.setOnClickListener {
            showTicketDetailsDialog(
                ticketId = "AMD-TKT-994101",
                passengerName = "Aarav Patel",
                time = "Today • 18:45",
                route = "THALTEJ → VASTRAL GAM",
                fare = "₹25.00",
                status = "COMPLETED",
                entryStation = "Thaltej (Gate 2)",
                exitStation = "Vastral Gam (Gate 1)",
                canRefund = false
            )
        }

        // Ticket 2: Kabir Mehta
        findViewById<CardView>(R.id.card_ticket_2)?.setOnClickListener {
            showTicketDetailsDialog(
                ticketId = "AMD-TKT-994102",
                passengerName = "Kabir Mehta",
                time = "Today • 18:32",
                route = "SABARMATI → OLD HIGH COURT",
                fare = "₹15.00",
                status = "PENDING",
                entryStation = "Sabarmati (Awaiting Entry)",
                exitStation = "Old High Court",
                canRefund = true
            )
        }

        // Ticket 3: Priya Sharma
        findViewById<CardView>(R.id.card_ticket_3)?.setOnClickListener {
            showTicketDetailsDialog(
                ticketId = "AMD-TKT-994103",
                passengerName = "Priya Sharma",
                time = "Today • 18:10",
                route = "VASTRAL GAM → THALTEJ",
                fare = "₹25.00",
                status = "COMPLETED",
                entryStation = "Vastral Gam (Gate 3)",
                exitStation = "Thaltej (Gate 2)",
                canRefund = false
            )
        }
    }

    private fun showTicketDetailsDialog(
        ticketId: String,
        passengerName: String,
        time: String,
        route: String,
        fare: String,
        status: String,
        entryStation: String,
        exitStation: String,
        canRefund: Boolean
    ) {
        val builder = AlertDialog.Builder(this)
            .setTitle("Ticket Details: $ticketId")
            .setMessage(
                "Passenger: $passengerName\n" +
                "Issued: $time\n" +
                "Route: $route\n" +
                "Fare: $fare\n" +
                "Status: $status\n" +
                "Entry: $entryStation\n" +
                "Exit: $exitStation"
            )

        if (canRefund) {
            builder.setNeutralButton("Cancel & Refund") { _, _ ->
                Toast.makeText(this, "Ticket $ticketId cancelled and $fare refunded to passenger", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("Validate Turnstile") { _, _ ->
                Toast.makeText(this, "Turnstile entry validated for $passengerName ($ticketId)", Toast.LENGTH_SHORT).show()
            }
        } else {
            builder.setPositiveButton("Verify QR Code") { _, _ ->
                Toast.makeText(this, "QR Code $ticketId is valid and verified", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Close", null)
        builder.show()
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

        // Tab 3: Settings
        findViewById<LinearLayout>(R.id.nav_admin_settings)?.setOnClickListener {
            val intent = Intent(this, AdminSettingsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }
    }
}
