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
 * AdminStationsActivity — Alok
 * Admin management screen for metro station data.
 * Displays a list of all stations with line info,
 * options to view details, add, or decommission stations.
 */
class AdminStationsActivity : AppCompatActivity() {

    private val stations = listOf(
        Triple("Thaltej", "Blue Line (E-W)", "Active"),
        Triple("Gujarat University", "Blue Line (E-W)", "Active"),
        Triple("Paldi", "Blue Line (E-W)", "Active"),
        Triple("Gandhigram", "Blue Line (E-W)", "Active"),
        Triple("Kalupur Railway Station", "Blue Line (E-W)", "Active"),
        Triple("Old High Court", "Blue Line (E-W)", "Active"),
        Triple("Vastral Gam", "Blue Line (E-W)", "Active"),
        Triple("Motera Stadium", "Red Line (N-S)", "Active"),
        Triple("Sabarmati", "Red Line (N-S)", "Active"),
        Triple("Ranip", "Red Line (N-S)", "Active"),
        Triple("APMC", "Red Line (N-S)", "Active"),
        Triple("Naroda", "Red Line (N-S)", "Active"),
        Triple("Odhav", "Red Line (N-S)", "Active")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stations)

        setupHeader()
        loadStationList()
        setupBottomNav()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_stations_back)?.setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_stations_title)?.text = "Station Management"

        // Total count badge
        findViewById<TextView>(R.id.tv_stations_count)?.text = "${stations.size} Stations"

        // Add station FAB
        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab_add_station)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Add New Station")
                .setMessage("Station addition requires system engineering approval.\nPlease submit a request via the Metro Operations Portal.")
                .setPositiveButton("Request Access", null)
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun loadStationList() {
        val container = findViewById<LinearLayout>(R.id.ll_stations_container) ?: return
        container.removeAllViews()
        val inflater = layoutInflater

        for ((name, line, status) in stations) {
            val card = inflater.inflate(R.layout.item_station_card, container, false)
            card.findViewById<TextView>(R.id.tv_station_name)?.text = name
            card.findViewById<TextView>(R.id.tv_station_line)?.text = line
            card.findViewById<TextView>(R.id.tv_station_status)?.text = status

            card.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Station: $name")
                    .setMessage(
                        "Line: $line\n" +
                        "Status: $status\n" +
                        "Gates: 4 active\n" +
                        "Daily Avg Passengers: ~1,200\n" +
                        "Last Maintenance: 14 Oct 2026"
                    )
                    .setPositiveButton("View Full Report") { _, _ ->
                        Toast.makeText(this, "Loading $name station report…", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Close", null)
                    .show()
            }
            container.addView(card)
        }
    }

    private fun setupBottomNav() {
        findViewById<LinearLayout>(R.id.nav_admin_home)?.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            })
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_admin_users)?.setOnClickListener {
            startActivity(Intent(this, AdminUsersActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            })
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_admin_settings)?.setOnClickListener {
            startActivity(Intent(this, AdminSettingsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            })
            finish()
        }
    }
}
