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
 * Admin Revenue Details Activity
 * Displays comprehensive revenue metrics, breakdowns (UPI, Cash, Card), and detailed transactions.
 */
class AdminRevenueActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_revenue)

        setupHeader()
        setupHeroCard()
        setupBreakdownCards()
        setupTransactions()
        setupBottomNav()
    }

    private fun setupHeader() {
        // Circular Back Button Click
        findViewById<ImageView>(R.id.btn_revenue_back)?.setOnClickListener {
            finish()
        }

        // Bell Notification Click
        findViewById<ImageView>(R.id.iv_revenue_bell)?.setOnClickListener {
            Toast.makeText(this, "Revenue Alert: Morning rush generated ₹18,200 via UPI", Toast.LENGTH_SHORT).show()
        }

        // Profile Avatar Click
        findViewById<ImageView>(R.id.iv_revenue_profile)?.setOnClickListener {
            Toast.makeText(this, "Admin Session: Financial Operations Manager", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupHeroCard() {
        findViewById<CardView>(R.id.card_hero_revenue)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Today's Revenue Summary")
                .setMessage("Total Collected: ₹45,210.00\nTickets Issued: 1,240\nYesterday's Total: ₹46,321.00 (-2.4%)\nAverage Fare: ₹36.45 per ticket")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun setupBreakdownCards() {
        // Card 1: UPI
        findViewById<LinearLayout>(R.id.card_breakdown_upi)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("UPI Collections (₹28.4k)")
                .setMessage("Share: 62.8% of daily revenue\n• Google Pay: ₹12,496 (44%)\n• PhonePe: ₹10,792 (38%)\n• Paytm / Other UPI: ₹5,112 (18%)\nTotal UPI Transactions: 778")
                .setPositiveButton("Close", null)
                .show()
        }

        // Card 2: Cash
        findViewById<LinearLayout>(R.id.card_breakdown_cash)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cash Collections (₹12.1k)")
                .setMessage("Share: 26.7% of daily revenue\n• Station Counter 1: ₹6,800\n• Station Counter 2: ₹5,300\nTotal Cash Tickets: 332")
                .setPositiveButton("Close", null)
                .show()
        }

        // Card 3: Card
        findViewById<LinearLayout>(R.id.card_breakdown_card)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Metro Smart Card (₹4.7k)")
                .setMessage("Share: 10.5% of daily revenue\n• Ahmedabad Metro Smart Card Recharges: ₹3,500\n• RuPay Transit Cards: ₹1,200\nTotal Card Transactions: 130")
                .setPositiveButton("Close", null)
                .show()
        }
    }

    private fun setupTransactions() {
        // View All click
        findViewById<TextView>(R.id.tv_revenue_view_all)?.setOnClickListener {
            Toast.makeText(this, "Showing all 1,240 transactions for today", Toast.LENGTH_SHORT).show()
        }

        // Transaction 1: Aarav Patel
        findViewById<CardView>(R.id.card_rev_trans_1)?.setOnClickListener {
            showTransactionDialog(
                txnId = "AMD-TXN-20260901",
                name = "Aarav Patel",
                time = "Today • 18:45",
                route = "Thaltej → vastral Gam",
                fare = "₹25.00",
                method = "UPI (Google Pay)",
                status = "Completed"
            )
        }

        // Transaction 2: Kabir Mehta
        findViewById<CardView>(R.id.card_rev_trans_2)?.setOnClickListener {
            showTransactionDialog(
                txnId = "AMD-TXN-20260902",
                name = "Kabir Mehta",
                time = "Today • 18:32",
                route = "Sabarmati → Old High Court",
                fare = "₹15.00",
                method = "UPI (Pending Gateway Response)",
                status = "Pending"
            )
        }

        // Transaction 3: Priya Sharma
        findViewById<CardView>(R.id.card_rev_trans_3)?.setOnClickListener {
            showTransactionDialog(
                txnId = "AMD-TXN-20260903",
                name = "Priya Sharma",
                time = "Today • 18:15",
                route = "Motera Stadium → Paldi (2 Tickets)",
                fare = "₹40.00",
                method = "Credit/Debit Card",
                status = "Completed"
            )
        }

        // Transaction 4: Raj Sharma
        findViewById<CardView>(R.id.card_rev_trans_4)?.setOnClickListener {
            showTransactionDialog(
                txnId = "AMD-TXN-20260904",
                name = "Raj Sharma",
                time = "Today • 17:50",
                route = "Thaltej → Vastral Gam",
                fare = "₹25.00",
                method = "Cash (Counter 1)",
                status = "Completed"
            )
        }
    }

    private fun showTransactionDialog(
        txnId: String,
        name: String,
        time: String,
        route: String,
        fare: String,
        method: String,
        status: String
    ) {
        AlertDialog.Builder(this)
            .setTitle("Transaction Details")
            .setMessage("ID: $txnId\nPassenger: $name\nTime: $time\nRoute: $route\nPayment Method: $method\nAmount: $fare\nStatus: $status")
            .setPositiveButton("Print Receipt") { _, _ ->
                Toast.makeText(this, "Receipt generated for $txnId", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Close", null)
            .show()
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
