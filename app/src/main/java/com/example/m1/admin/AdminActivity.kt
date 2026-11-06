package com.example.m1.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.m1.MetroData
import com.example.m1.R

/**
 * Admin Panel Activity
 * Dedicated Admin Panel screen matching the requested System Overview design.
 * Includes station management actions: Add Station & Change Station Name.
 */
class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        MetroData.loadStations(this)

        setupHeader()
        setupCards()
        setupTransactions()
        setupStationButtons()
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

    private fun setupStationButtons() {
        // Button 1: Add Station
        findViewById<Button>(R.id.btn_admin_add_station)?.setOnClickListener {
            showAddStationDialog()
        }

        // Button 2: Change Station Name
        findViewById<Button>(R.id.btn_admin_change_station_name)?.setOnClickListener {
            showChangeStationNameDialog()
        }
    }

    /**
     * Dialog to Add a New Metro Station
     */
    private fun showAddStationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_admin_add_station, null)
        val etStationName = dialogView.findViewById<EditText>(R.id.et_new_station_name)
        val spinnerLine = dialogView.findViewById<Spinner>(R.id.spinner_station_line)

        val lines = listOf("Blue Line (East-West)", "Red Line (North-South)")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lines)
        spinnerLine.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Metro Station")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val saveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            saveBtn.setOnClickListener {
                val name = etStationName.text.toString().trim()
                if (name.length < 2) {
                    etStationName.error = "Please enter a valid station name"
                    return@setOnClickListener
                }

                val selectedLine = spinnerLine.selectedItem.toString()
                val isAdded = MetroData.addStation(this, name, selectedLine)
                if (isAdded) {
                    Toast.makeText(this, "Station '$name' added successfully!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    etStationName.error = "Station '$name' already exists"
                    Toast.makeText(this, "Station already exists in network!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    /**
     * Dialog to Change Existing Metro Station Name
     */
    private fun showChangeStationNameDialog() {
        val stations = MetroData.getAllStations(this)
        if (stations.isEmpty()) {
            Toast.makeText(this, "No stations available to rename", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_admin_change_station, null)
        val spinnerStations = dialogView.findViewById<Spinner>(R.id.spinner_existing_stations)
        val etNewName = dialogView.findViewById<EditText>(R.id.et_updated_station_name)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stations)
        spinnerStations.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setTitle("Change Station Name")
            .setView(dialogView)
            .setPositiveButton("Update", null)
            .setNegativeButton("Cancel") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val updateBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            updateBtn.setOnClickListener {
                val oldName = spinnerStations.selectedItem.toString()
                val newName = etNewName.text.toString().trim()

                if (newName.length < 2) {
                    etNewName.error = "Please enter a valid station name"
                    return@setOnClickListener
                }
                if (newName.equals(oldName, ignoreCase = true)) {
                    etNewName.error = "New name cannot be identical to current name"
                    return@setOnClickListener
                }

                val isUpdated = MetroData.renameStation(this, oldName, newName)
                if (isUpdated) {
                    Toast.makeText(this, "Station '$oldName' renamed to '$newName'!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    etNewName.error = "A station named '$newName' already exists"
                    Toast.makeText(this, "Station '$newName' already exists in network!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
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

        // Long press on brand header opens quick stations manager
        findViewById<TextView>(R.id.tv_admin_brand)?.setOnLongClickListener {
            startActivity(Intent(this, AdminStationsActivity::class.java))
            true
        }
    }
}
