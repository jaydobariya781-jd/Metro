package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton

/**
 * BookTicketActivity — Brij Nagvadiya
 * Allows the user to:
 *  1. Select FROM and TO stations via spinners
 *  2. Adjust passenger count (1–6)
 *  3. View dynamically calculated fare
 *  4. Proceed to PaymentActivity
 */
class BookTicketActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var tvPassengerCount: TextView
    private lateinit var tvFareAmount: TextView
    private lateinit var btnMinus: MaterialButton
    private lateinit var btnPlus: MaterialButton
    private lateinit var btnProceedToPayment: MaterialButton
    private lateinit var tvDistance: TextView

    private var passengerCount = 1
    private var fromStation = ""
    private var toStation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_ticket)

        initViews()
        setupHeader()
        setupSpinners()
        setupPassengerControls()
        setupProceedButton()
    }

    private fun initViews() {
        spinnerFrom = findViewById(R.id.spinner_from_station)
        spinnerTo = findViewById(R.id.spinner_to_station)
        tvPassengerCount = findViewById(R.id.tv_passenger_count)
        tvFareAmount = findViewById(R.id.tv_fare_amount)
        btnMinus = findViewById(R.id.btn_passenger_minus)
        btnPlus = findViewById(R.id.btn_passenger_plus)
        btnProceedToPayment = findViewById(R.id.btn_proceed_payment)
        tvDistance = findViewById(R.id.tv_route_distance)
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_book_back)?.setOnClickListener { finish() }
    }

    private fun setupSpinners() {
        val stations = MetroData.allStations
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        // Default: set different stations
        spinnerTo.setSelection(5)
        fromStation = stations[0]
        toStation = stations[5]

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fromStation = spinnerFrom.selectedItem.toString()
                toStation = spinnerTo.selectedItem.toString()
                updateFare()
            }
        }
        spinnerFrom.onItemSelectedListener = spinnerListener
        spinnerTo.onItemSelectedListener = spinnerListener

        // Swap stations button
        findViewById<ImageView>(R.id.btn_swap_stations)?.setOnClickListener {
            val fromPos = spinnerFrom.selectedItemPosition
            val toPos = spinnerTo.selectedItemPosition
            spinnerFrom.setSelection(toPos)
            spinnerTo.setSelection(fromPos)
        }
    }

    private fun setupPassengerControls() {
        tvPassengerCount.text = passengerCount.toString()
        btnMinus.setOnClickListener {
            if (passengerCount > 1) {
                passengerCount--
                tvPassengerCount.text = passengerCount.toString()
                updateFare()
            } else {
                Toast.makeText(this, "Minimum 1 passenger", Toast.LENGTH_SHORT).show()
            }
        }
        btnPlus.setOnClickListener {
            if (passengerCount < 6) {
                passengerCount++
                tvPassengerCount.text = passengerCount.toString()
                updateFare()
            } else {
                Toast.makeText(this, "Maximum 6 passengers per booking", Toast.LENGTH_SHORT).show()
            }
        }
        updateFare()
    }

    private fun updateFare() {
        if (fromStation == toStation) {
            tvFareAmount.text = "₹10.00"
            tvDistance?.text = "Same station"
            return
        }
        val fare = MetroData.calculateFare(fromStation, toStation, passengerCount)
        tvFareAmount.text = "₹${"%.2f".format(fare)}"
        val line = MetroData.getStationLine(fromStation)
        tvDistance?.text = line
    }

    private fun setupProceedButton() {
        btnProceedToPayment.setOnClickListener {
            if (fromStation == toStation) {
                Toast.makeText(this, "Origin and destination cannot be the same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val fare = MetroData.calculateFare(fromStation, toStation, passengerCount)
            val intent = Intent(this, PaymentActivity::class.java).apply {
                putExtra("FROM_STATION", fromStation)
                putExtra("TO_STATION", toStation)
                putExtra("PASSENGERS", passengerCount)
                putExtra("FARE", fare)
            }
            startActivity(intent)
        }
    }
}
