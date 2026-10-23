package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/**
 * SearchTrainsActivity — Brij Nagvadiya
 * Allows users to search for metro routes between stations.
 * Displays estimated travel time, fare, and transfers.
 */
class SearchTrainsActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_trains)

        spinnerFrom = findViewById(R.id.spinner_search_from)
        spinnerTo = findViewById(R.id.spinner_search_to)
        tvResult = findViewById(R.id.tv_search_result)

        setupHeader()
        setupSpinners()
        setupSearchButton()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_search_back)?.setOnClickListener { finish() }
    }

    private fun setupSpinners() {
        val stations = MetroData.allStations
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
        spinnerTo.setSelection(8)
    }

    private fun setupSearchButton() {
        val openBooking = {
            val fromStation = spinnerFrom.selectedItem.toString()
            val toStation = spinnerTo.selectedItem.toString()
            val intent = Intent(this, BookTicketActivity::class.java)
            startActivity(intent)
        }

        findViewById<MaterialButton>(R.id.btn_search_trains)?.setOnClickListener {
            val from = spinnerFrom.selectedItem.toString()
            val to = spinnerTo.selectedItem.toString()
            if (from == to) {
                tvResult.text = "⚠ Please select different stations"
                return@setOnClickListener
            }
            val fare = MetroData.calculateFare(from, to, 1)
            val line = MetroData.getStationLine(from)
            tvResult.text = "Route: $from → $to\nLine: $line\nEstimated Fare: ₹${"%.2f".format(fare)}\nEstimated Time: ~${(fare * 1.2).toInt()} mins"
        }

        findViewById<MaterialButton>(R.id.btn_book_from_search)?.setOnClickListener {
            openBooking()
        }
    }
}
