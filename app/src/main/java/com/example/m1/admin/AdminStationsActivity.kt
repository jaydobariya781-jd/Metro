package com.example.m1.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.m1.MetroData
import com.example.m1.R

/**
 * AdminStationsActivity — Alok
 * Admin management screen for metro station data.
 * Displays all stations with line info, with live add and edit capabilities.
 */
class AdminStationsActivity : AppCompatActivity() {

    private var stationList: List<Triple<String, String, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stations)

        MetroData.loadStations(this)
        setupHeader()
        loadStationList()
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        loadStationList()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_stations_back)?.setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_stations_title)?.text = "Station Management"

        // Add station FAB
        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab_add_station)?.setOnClickListener {
            showAddStationDialog()
        }
    }

    private fun loadStationList() {
        val allStationNames = MetroData.getAllStations(this)
        stationList = allStationNames.map { name ->
            val line = MetroData.getStationLine(name)
            Triple(name, line, "Active")
        }

        findViewById<TextView>(R.id.tv_stations_count)?.text = "${stationList.size} Stations"

        val container = findViewById<LinearLayout>(R.id.ll_stations_container) ?: return
        container.removeAllViews()
        val inflater = layoutInflater

        for ((name, line, status) in stationList) {
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
                    .setPositiveButton("Rename Station") { _, _ ->
                        showRenameDialog(name)
                    }
                    .setNegativeButton("Close", null)
                    .show()
            }
            container.addView(card)
        }
    }

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
                    loadStationList()
                    dialog.dismiss()
                } else {
                    etStationName.error = "Station '$name' already exists"
                    Toast.makeText(this, "Station already exists!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    private fun showRenameDialog(oldName: String) {
        val input = EditText(this).apply {
            setText(oldName)
            setSelection(oldName.length)
            hint = "New station name"
        }

        AlertDialog.Builder(this)
            .setTitle("Rename Station: $oldName")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.length >= 2 && !newName.equals(oldName, ignoreCase = true)) {
                    val updated = MetroData.renameStation(this, oldName, newName)
                    if (updated) {
                        Toast.makeText(this, "Station renamed to '$newName'", Toast.LENGTH_SHORT).show()
                        loadStationList()
                    } else {
                        Toast.makeText(this, "Station '$newName' already exists", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
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
