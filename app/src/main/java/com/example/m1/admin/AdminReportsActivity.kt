package com.example.m1.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.m1.R

/**
 * AdminReportsActivity — Alok
 * Admin Reports & Analytics dashboard.
 * Shows weekly/monthly revenue, passenger trends, peak hours,
 * and allows report exports for management review.
 */
class AdminReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_reports)

        setupHeader()
        setupKpiCards()
        setupWeeklyTrend()
        setupPeakHours()
        setupExports()
        setupBottomNav()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_reports_back)?.setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_reports_title)?.text = "Reports & Analytics"
        findViewById<TextView>(R.id.tv_reports_date_range)?.text = "Week: 16 Oct – 23 Oct 2026"
    }

    private fun setupKpiCards() {
        // Weekly Revenue Card
        findViewById<LinearLayout>(R.id.card_weekly_revenue)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Weekly Revenue Breakdown")
                .setMessage(
                    "Total: ₹3,12,450.00\n\n" +
                    "• Monday:    ₹41,200\n" +
                    "• Tuesday:   ₹43,800\n" +
                    "• Wednesday: ₹45,100\n" +
                    "• Thursday:  ₹44,600\n" +
                    "• Friday:    ₹48,900\n" +
                    "• Saturday:  ₹52,400\n" +
                    "• Sunday:    ₹36,450\n\n" +
                    "Best Day: Saturday (+8.2% vs Mon)\n" +
                    "YoY Growth: +14.3%"
                )
                .setPositiveButton("Export PDF", null)
                .setNegativeButton("Close", null)
                .show()
        }

        // Monthly Passengers Card
        findViewById<LinearLayout>(R.id.card_monthly_passengers)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Monthly Passenger Count — October 2026")
                .setMessage(
                    "Total: 3,84,200 passengers\n\n" +
                    "• Blue Line (E-W): 2,12,400 (55.3%)\n" +
                    "• Red Line (N-S):  1,71,800 (44.7%)\n\n" +
                    "Peak Station: Old High Court\n" +
                    "Avg Daily: 12,393 passengers\n" +
                    "MoM Change: +6.1%"
                )
                .setPositiveButton("Full Report", null)
                .setNegativeButton("Close", null)
                .show()
        }

        // Cancellation Rate Card
        findViewById<LinearLayout>(R.id.card_cancellation_rate)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cancellation & Refund Analysis")
                .setMessage(
                    "Weekly Cancellations: 142 tickets\n" +
                    "Cancellation Rate: 1.65%\n\n" +
                    "Top Reasons:\n" +
                    "• Self-cancel (app): 89 (62.7%)\n" +
                    "• Gate timeout: 31 (21.8%)\n" +
                    "• Payment reversal: 22 (15.5%)\n\n" +
                    "Total Refunded: ₹4,150.00\n" +
                    "Avg Refund Time: 3.2 minutes"
                )
                .setPositiveButton("Refund Log", null)
                .setNegativeButton("Close", null)
                .show()
        }
    }

    private fun setupWeeklyTrend() {
        // View All Trends
        findViewById<TextView>(R.id.tv_view_trend)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Passenger Trend — Last 4 Weeks")
                .setMessage(
                    "Week 1 (25 Sep – 1 Oct):  82,400 passengers\n" +
                    "Week 2 (2 Oct – 8 Oct):   87,900 passengers\n" +
                    "Week 3 (9 Oct – 15 Oct):  90,200 passengers\n" +
                    "Week 4 (16 Oct – 22 Oct): 95,600 passengers\n\n" +
                    "4-Week Growth: +16.0%\n" +
                    "Trend: ↑ Consistent increase"
                )
                .setPositiveButton("Download CSV", null)
                .setNegativeButton("Close", null)
                .show()
        }
    }

    private fun setupPeakHours() {
        // Peak Hours Analysis
        findViewById<LinearLayout>(R.id.card_peak_hours)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Peak Hour Analysis — Today")
                .setMessage(
                    "Morning Rush:\n" +
                    "• 07:30–09:30 AM: 320 tickets/hr (PEAK)\n" +
                    "• 09:30–11:00 AM: 180 tickets/hr\n\n" +
                    "Afternoon:\n" +
                    "• 11:00–05:00 PM: 80–120 tickets/hr\n\n" +
                    "Evening Rush:\n" +
                    "• 05:30–08:00 PM: 290 tickets/hr (PEAK)\n" +
                    "• 08:00–10:00 PM: 140 tickets/hr\n\n" +
                    "Off-peak: 10:00 PM – 07:30 AM"
                )
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun setupExports() {
        // Export weekly PDF
        findViewById<TextView>(R.id.btn_export_pdf)?.setOnClickListener {
            Toast.makeText(this, "Exporting Weekly_Report_Oct2026.pdf…", Toast.LENGTH_LONG).show()
        }
        // Export CSV
        findViewById<TextView>(R.id.btn_export_csv)?.setOnClickListener {
            Toast.makeText(this, "Exporting Ticket_Data_Oct2026.csv…", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupBottomNav() {
        findViewById<LinearLayout>(R.id.nav_admin_home)?.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT })
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_admin_users)?.setOnClickListener {
            startActivity(Intent(this, AdminUsersActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT })
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_admin_settings)?.setOnClickListener {
            startActivity(Intent(this, AdminSettingsActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT })
            finish()
        }
    }
}
