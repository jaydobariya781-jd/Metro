package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/**
 * SearchTrainsActivity — Brij Nagvadiya
 * Allows users to search for metro routes between stations.
 * Displays estimated travel time, fare, and transfers.
 */
class SearchTrainsActivity : AppCompatActivity() {

    private lateinit var tvStartStation: TextView
    private lateinit var tvEndStation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_trains)

        tvStartStation = findViewById(R.id.tv_start_station)
        tvEndStation = findViewById(R.id.tv_end_station)

        setupHeader()
        setupClickListeners()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener { finish() }
    }

    private fun setupClickListeners() {
        val openBooking = {
            val fromStation = tvStartStation.text.toString()
            val toStation = tvEndStation.text.toString()
            val intent = Intent(this, BookTicketActivity::class.java).apply {
                putExtra("FROM_STATION", fromStation)
                putExtra("TO_STATION", toStation)
            }
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.btn_swap)?.setOnClickListener {
            val temp = tvStartStation.text
            tvStartStation.text = tvEndStation.text
            tvEndStation.text = temp
        }

        findViewById<MaterialButton>(R.id.btn_book1)?.setOnClickListener { openBooking() }
        findViewById<MaterialButton>(R.id.btn_book2)?.setOnClickListener { openBooking() }
        findViewById<View>(R.id.btn_pay_now)?.setOnClickListener { openBooking() }
    }
}
