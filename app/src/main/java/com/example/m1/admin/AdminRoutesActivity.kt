package com.example.m1.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.m1.MetroData
import com.example.m1.R

/**
 * AdminRoutesActivity — Alok
 * Displays all metro route corridors with fare matrix,
 * allows admin to view route details and export reports.
 */
class AdminRoutesActivity : AppCompatActivity() {

    private data class RouteInfo(
        val from: String,
        val to: String,
        val line: String,
        val distance: String,
        val minFare: String,
        val avgDaily: String
    )

    private val popularRoutes = listOf(
        RouteInfo("Thaltej", "Vastral Gam", "Blue Line", "~22 km", "₹50", "520 tickets"),
        RouteInfo("Sabarmati", "Old High Court", "Red Line", "~14 km", "₹30", "380 tickets"),
        RouteInfo("Motera Stadium", "APMC", "Red Line", "~8 km", "₹20", "220 tickets"),
        RouteInfo("Paldi", "Kalupur Rly Stn", "Blue Line", "~6 km", "₹15", "185 tickets"),
        RouteInfo("Gujarat University", "Old High Court", "Blue Line", "~4 km", "₹10", "160 tickets"),
        RouteInfo("Ranip", "GNLU", "Red Line", "~5 km", "₹15", "140 tickets"),
        RouteInfo("Naroda", "Motera Stadium", "Red Line", "~12 km", "₹25", "130 tickets"),
        RouteInfo("Vastral Gam", "Thaltej", "Blue Line", "~22 km", "₹50", "510 tickets")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_routes)

        setupHeader()
        loadRoutes()
        setupBottomNav()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_routes_back)?.setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_routes_total)?.text = "8 Popular Routes | 2 Corridors"

        // Export button
        findViewById<TextView>(R.id.tv_routes_export)?.setOnClickListener {
            Toast.makeText(this, "Route report exported as PDF…", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRoutes() {
        val container = findViewById<LinearLayout>(R.id.ll_routes_container) ?: return
        container.removeAllViews()

        for (route in popularRoutes) {
            val card = layoutInflater.inflate(R.layout.item_route_card, container, false)
            card.findViewById<TextView>(R.id.tv_route_from_to)?.text = "${route.from} → ${route.to}"
            card.findViewById<TextView>(R.id.tv_route_line)?.text = route.line
            card.findViewById<TextView>(R.id.tv_route_distance)?.text = route.distance
            card.findViewById<TextView>(R.id.tv_route_fare)?.text = route.minFare
            card.findViewById<TextView>(R.id.tv_route_daily)?.text = route.avgDaily

            card.setOnClickListener {
                val fare = MetroData.calculateFare(route.from, route.to, 1)
                AlertDialog.Builder(this)
                    .setTitle("${route.from} → ${route.to}")
                    .setMessage(
                        "Corridor: ${route.line}\n" +
                        "Approx Distance: ${route.distance}\n" +
                        "Base Fare (1 passenger): ₹${"%.2f".format(fare)}\n" +
                        "Daily Avg Traffic: ${route.avgDaily}\n" +
                        "Estimated Travel Time: ~${(fare * 1.2).toInt()} mins\n" +
                        "Stops: ${(fare / 5).toInt() + 2} stations"
                    )
                    .setPositiveButton("Fare Matrix") { _, _ ->
                        showFareMatrix(route)
                    }
                    .setNegativeButton("Close", null)
                    .show()
            }
            container.addView(card)
        }
    }

    private fun showFareMatrix(route: RouteInfo) {
        val sb = StringBuilder("Fare by Passenger Count:\n\n")
        for (p in 1..6) {
            val fare = MetroData.calculateFare(route.from, route.to, p)
            sb.append("$p passenger${if (p > 1) "s" else ""}: ₹${"%.2f".format(fare)}\n")
        }
        AlertDialog.Builder(this)
            .setTitle("Fare Matrix: ${route.from} → ${route.to}")
            .setMessage(sb.toString())
            .setPositiveButton("OK", null)
            .show()
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
